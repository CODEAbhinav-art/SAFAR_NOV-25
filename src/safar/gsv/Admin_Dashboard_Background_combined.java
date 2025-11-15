package safar.gsv;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;

public class Admin_Dashboard_Background_try {

	private static final Scanner sc = new Scanner(System.in);
    private static final double FARE_PER_KM = 2.5; // Default fallback changeable
    
    // Define a constant for easy testing/debugging
    private static final String TEST_USER_ID = "guest_user_1";
    private static final String TEST_PASSWORD = "password123";

    // --- Utility Input Helper ---

    /**
     * Reads a line from the scanner and attempts to parse it as an Integer.
     */
    private static int getIntegerInput(String prompt, int defaultValue) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        if (line.isEmpty() && defaultValue != Integer.MIN_VALUE) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid number format. Please try again.");
            return Integer.MIN_VALUE; 
        }
    }

    // --- Core Helper Functions ---

    private static Time parseTimeOrNull(String s) {
        if (s == null || s.trim().isEmpty()) return null;
        String timeStr = s.trim();
        try {
            if (timeStr.length() == 5) timeStr = timeStr + ":00";
            return Time.valueOf(timeStr);
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ Invalid time format '" + s + "'. Expected HH:MM or HH:MM:SS. Using null.");
            return null;
        }
    }

    private static int getNextSequenceForRoute(Connection con, int routeId) throws SQLException {
        String q = "SELECT MAX(sequence_no) as mx FROM stations WHERE route_id = ?";
        try (PreparedStatement ps = con.prepareStatement(q)) { 
            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int mx = rs.getInt("mx");
                return mx + 1;
            }
        }
        return 1;
    }
    
    // --- Display/View Methods ---

    public static void displayTransports() {
        String sql = "SELECT * FROM transports";
        try (Connection con = Jdbc_Connection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nTransport ID | Type   | Number    | Name               | Seats | Fare/Km");
            System.out.println("-------------------------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-12d %-7s %-10s %-20s %-6d %-7.2f%n",
                    rs.getInt("transport_id"),
                    rs.getString("transport_type"),
                    rs.getString("transport_no"),
                    rs.getString("name"),
                    rs.getInt("total_seats"),
                    rs.getDouble("base_fare_per_km"));
            }
            System.out.println("-------------------------------------------------------------------------");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void displayRoutes() {
        String sql = "SELECT r.route_id, t.transport_type, t.transport_no, r.source, r.destination " +
                     "FROM routes r LEFT JOIN transports t ON r.transport_id = t.transport_id";
        try (Connection con = Jdbc_Connection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nRoute ID | Type  | No.     | Source -> Destination");
            System.out.println("----------------------------------------------------------");
            while (rs.next()) {
                System.out.printf("%-8d %-6s %-8s %s -> %s%n",
                    rs.getInt("route_id"),
                    rs.getString("transport_type"),
                    rs.getString("transport_no"),
                    rs.getString("source"),
                    rs.getString("destination"));
            }
            System.out.println("----------------------------------------------------------");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // --- Internal CRUD Helpers (Used by Wizard) ---

    private static int internalAddTransport(String type, String number, String name, int totalSeats, double baseFare) {
        String sql = "INSERT INTO transports (transport_type, transport_no, name, total_seats, base_fare_per_km) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, type);
            ps.setString(2, number);
            ps.setString(3, name.isEmpty() ? null : name);
            ps.setInt(4, totalSeats);
            ps.setDouble(5, baseFare);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) { System.out.println("Transport creation failed: " + e.getMessage()); }
        return -1;
    }
    
    private static int internalAddRoute(int tid, String src, String dest) {
        String sql = "INSERT INTO routes (transport_id, source, destination) VALUES (?, ?, ?)";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tid);
            ps.setString(2, src);
            ps.setString(3, dest);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Route creation failed: Duplicate route for this transport.");
        } catch (SQLException e) { 
            System.out.println("Route creation failed: " + e.getMessage()); 
        }
        return -1;
    }

    private static boolean internalAddStations(int routeId, List<String[]> stationData) {
        String sql = "INSERT INTO stations (route_id, station_name, sequence_no, distance_from_source, arrival_time, departure_time) VALUES (?, ?, ?, ?, ?, ?)";
        Connection con = null; 
        try {
            con = Jdbc_Connection.getConnection();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(sql)) { 
                int seq = getNextSequenceForRoute(con, routeId);
                for (String[] parts : stationData) {
                    ps.setInt(1, routeId);
                    ps.setString(2, parts[0]); 
                    ps.setInt(3, seq++);
                    ps.setDouble(4, Double.parseDouble(parts[1])); 
                    ps.setTime(5, parseTimeOrNull(parts[2])); 
                    ps.setTime(6, parseTimeOrNull(parts[3])); 
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
                return true;
            }
        } catch (SQLException e) { 
            System.out.println("Stations batch insertion failed: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {}
            }
            return false;
        } catch (NumberFormatException e) {
             System.out.println("Stations batch insertion failed: Invalid distance or time format.");
             if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {}
             }
             return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) {}
            }
        }
    }
    
    // --- Segment Generation Logic ---

    private static void generateSegmentsForSingleSchedule(Connection con, int routeId, int scheduleId, int totalSeats) throws SQLException {
        
        String getBaseFareSql = "SELECT t.base_fare_per_km FROM schedules s JOIN transports t ON s.transport_id = t.transport_id WHERE s.schedule_id = ?";
        double farePerKm = FARE_PER_KM; 
        
        try (PreparedStatement psFare = con.prepareStatement(getBaseFareSql)) {
            psFare.setInt(1, scheduleId);
            ResultSet rs = psFare.executeQuery();
            if (rs.next()) {
                farePerKm = rs.getDouble("base_fare_per_km");
            }
        }
        
        String getStationsSql = "SELECT station_id, distance_from_source, arrival_time, departure_time FROM stations WHERE route_id = ? ORDER BY sequence_no";
        List<Integer> stationIds = new ArrayList<>();
        List<Double> distances = new ArrayList<>();
        List<Time> arrivals = new ArrayList<>();
        List<Time> departures = new ArrayList<>();

        try (PreparedStatement psStations = con.prepareStatement(getStationsSql)) {
            psStations.setInt(1, routeId);
            ResultSet rs = psStations.executeQuery();
            while (rs.next()) {
                stationIds.add(rs.getInt("station_id"));
                distances.add(rs.getDouble("distance_from_source"));
                arrivals.add(rs.getTime("arrival_time"));
                departures.add(rs.getTime("departure_time"));
            }
        }

        if (stationIds.size() < 2) return; 

        String insertSeg = "INSERT INTO schedule_segments (schedule_id, start_station_id, end_station_id, distance_km, fare, seats_available) VALUES (?, ?, ?, ?, ?, ?)";
        
        // Ensure segment creation is atomic:
        boolean originalAutoCommit = con.getAutoCommit();
        con.setAutoCommit(false);
        
        try (PreparedStatement ps = con.prepareStatement(insertSeg)) {
            for (int i = 0; i < stationIds.size() - 1; i++) {
                for (int j = i + 1; j < stationIds.size(); j++) {
                    int start = stationIds.get(i);
                    int end = stationIds.get(j);
                    double dist = distances.get(j) - distances.get(i);
                    double fare = dist * farePerKm; 

                    ps.setInt(1, scheduleId);
                    ps.setInt(2, start);
                    ps.setInt(3, end);
                    ps.setDouble(4, dist);
                    ps.setDouble(5, fare);
                    ps.setInt(6, totalSeats); 
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            con.commit();
            con.setAutoCommit(originalAutoCommit);
        } catch (SQLException e) {
             con.rollback(); 
             con.setAutoCommit(originalAutoCommit);
             throw e; 
        }
    }


    // --- Internal Schedule Insertion (Used by Wizard) ---

    private static void internalInsertSingleSchedule(int tid, int routeId, int startStation, int endStation, 
                                             String depTimeStr, String arrTimeStr, LocalDate date, int totalSeats) {
        String sql = "INSERT INTO schedules (transport_id, route_id, start_station_id, end_station_id, departure_time, arrival_time, date, total_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, tid);
            ps.setInt(2, routeId);
            ps.setInt(3, startStation);
            ps.setInt(4, endStation);
            ps.setTime(5, parseTimeOrNull(depTimeStr));
            ps.setTime(6, parseTimeOrNull(arrTimeStr));
            ps.setDate(7, Date.valueOf(date));
            ps.setInt(8, totalSeats);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int scheduleId = rs.getInt(1);
                    generateSegmentsForSingleSchedule(con, routeId, scheduleId, totalSeats); 
                }
            }
            System.out.println("✅ Schedule and segments created for " + date);

        } catch (SQLException e) { System.out.println("Single schedule creation failed: " + e.getMessage()); }
    }

    private static void internalInsertMultipleSchedules(int tid, int routeId, int startStation, int endStation, 
                                                String depTimeStr, String arrTimeStr, List<LocalDate> dates, int totalSeats) {
        String sql = "INSERT INTO schedules (transport_id, route_id, start_station_id, end_station_id, departure_time, arrival_time, date, total_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection con = null; 
        
        try {
            con = Jdbc_Connection.getConnection();
            con.setAutoCommit(false); 

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) { 
                for (LocalDate d : dates) {
                    ps.setInt(1, tid);
                    ps.setInt(2, routeId);
                    ps.setInt(3, startStation);
                    ps.setInt(4, endStation);
                    ps.setTime(5, parseTimeOrNull(depTimeStr));
                    ps.setTime(6, parseTimeOrNull(arrTimeStr));
                    ps.setDate(7, Date.valueOf(d));
                    ps.setInt(8, totalSeats);
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
                
                List<Integer> newScheduleIds = new ArrayList<>();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    while (rs.next()) newScheduleIds.add(rs.getInt(1));
                }

                for (int scheduleId : newScheduleIds) {
                    generateSegmentsForSingleSchedule(con, routeId, scheduleId, totalSeats); 
                }
            }
            System.out.println("✅ " + dates.size() + "-day schedules and segments created.");
            
        } catch (SQLException e) { 
            System.out.println("Multiple schedule creation failed: " + e.getMessage());
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {}
            }
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) {}
            }
        }
    }
    
    // --- Maintenance Logic ---

    private static void ensureSchedulesForGroup(Connection con, int routeId, int tid, 
                                                int startStId, int endStId, Time depTime, Time arrTime, 
                                                int totalSeats, List<LocalDate> dates) throws SQLException {

        String insert = "INSERT IGNORE INTO schedules (transport_id, route_id, start_station_id, end_station_id, departure_time, arrival_time, date, total_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        List<Integer> newScheduleIds = new ArrayList<>();
        
        try (PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            con.setAutoCommit(false);
            for (LocalDate d : dates) {
                ps.setInt(1, tid);
                ps.setInt(2, routeId);
                ps.setInt(3, startStId);
                ps.setInt(4, endStId);
                ps.setTime(5, depTime);
                ps.setTime(6, arrTime);
                ps.setDate(7, Date.valueOf(d));
                ps.setInt(8, totalSeats);
                ps.addBatch();
            }
            ps.executeBatch();
            con.commit();
            con.setAutoCommit(true);
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                while (rs.next()) newScheduleIds.add(rs.getInt(1));
            }
        } catch (SQLException e) {
             con.rollback(); 
             throw e; 
        }
        
        for (int scheduleId : newScheduleIds) {
            generateSegmentsForSingleSchedule(con, routeId, scheduleId, totalSeats);
        }
    }
    
    public static void refresh60DayWindow() {
        System.out.println("Refreshing 60-day windows...");
        String groupsSql = "SELECT DISTINCT s.route_id, s.transport_id, t.transport_type, t.total_seats, s.start_station_id, s.end_station_id, s.departure_time, s.arrival_time FROM schedules s JOIN transports t ON s.transport_id = t.transport_id WHERE t.transport_type <> 'car' GROUP BY s.route_id, s.transport_id, t.transport_type, t.total_seats, s.start_station_id, s.end_station_id, s.departure_time, s.arrival_time";
        
        try (Connection con = Jdbc_Connection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(groupsSql)) {

            while (rs.next()) {
                int routeId = rs.getInt("route_id");
                int transportId = rs.getInt("transport_id");
                int totalSeats = rs.getInt("total_seats"); 
                int startStationId = rs.getInt("start_station_id");
                int endStationId = rs.getInt("end_station_id");
                Time depTime = rs.getTime("departure_time");
                Time arrTime = rs.getTime("arrival_time");


                String maxSql = "SELECT MAX(date) AS mx FROM schedules WHERE route_id = ? AND transport_id = ?";
                LocalDate maxDate = null;
                try (PreparedStatement ps = con.prepareStatement(maxSql)) {
                    ps.setInt(1, routeId);
                    ps.setInt(2, transportId);
                    ResultSet r2 = ps.executeQuery();
                    if (r2.next()) {
                        Date d = r2.getDate("mx");
                        if (d != null) maxDate = d.toLocalDate();
                    }
                }

                LocalDate today = LocalDate.now();
                LocalDate target = today.plusDays(59); 
                
                List<LocalDate> datesToInsert = new ArrayList<>();
                if (maxDate == null) {
                    for (int i = 0; i < 60; i++) datesToInsert.add(today.plusDays(i));
                } else {
                    LocalDate current = maxDate;
                    while (current.isBefore(target)) {
                        LocalDate next = current.plusDays(1);
                        datesToInsert.add(next);
                        current = next;
                    }
                }
                
                if (!datesToInsert.isEmpty()) {
                   ensureSchedulesForGroup(con, routeId, transportId, startStationId, endStationId, depTime, arrTime, totalSeats, datesToInsert);
                }
            }
            System.out.println("Refresh complete.");
        } catch (SQLException e) { e.printStackTrace(); }
    }


    // --- USER SETUP FUNCTION (NEW) ---
    /**
     * Ensures a specific test user exists in the 'users' table.
     */
    public static void ensureTestUserExists() {
        String checkSql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        String insertSql = "INSERT INTO users (user_id, password, role) VALUES (?, ?, 'user')";
        
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement psCheck = con.prepareStatement(checkSql)) {
             
            psCheck.setString(1, TEST_USER_ID);
            
            // 1. Check if user exists
            ResultSet rs = psCheck.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                // 2. Insert user if they don't exist
                try (PreparedStatement psInsert = con.prepareStatement(insertSql)) {
                    psInsert.setString(1, TEST_USER_ID);
                    psInsert.setString(2, TEST_PASSWORD); // Note: Should be hashed in production
                    psInsert.executeUpdate();
                    System.out.println("✅ User '" + TEST_USER_ID + "' created for booking tests.");
                    System.out.println("   (Password: " + TEST_PASSWORD + ")");
                }
            } else {
                System.out.println("✅ User '" + TEST_USER_ID + "' already exists.");
            }
            
            System.out.println("\n--- ACTION REQUIRED IN USER DASHBOARD ---");
            System.out.println("Ensure your User_Dashboard_Background.bookTicket() method uses this ID.");
            System.out.println("e.g., String userId = \"" + TEST_USER_ID + "\";");

        } catch (SQLException e) {
            System.out.println("❌ Error ensuring test user exists: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // --- CONSOLIDATED SETUP WIZARD ---

    /**
     * Combines Add Transport, Add Route, Add Stations, and Add Schedule into one interactive flow.
     */
    public static void runFullSetupWizard() {
        System.out.println("\n==============================================");
        System.out.println("         🚂 SAFAR ROUTE SETUP WIZARD ✈️");
        System.out.println("==============================================");
        
        // --- STEP 1: ADD TRANSPORT ---
        System.out.println("\n--- 1. Transport Details ---");
        System.out.print("Enter Transport Type (train/bus/flight/car): ");
        String type = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter Transport Number (e.g., TR101): ");
        String number = sc.nextLine().trim();
        System.out.print("Enter Transport Name (e.g., SF Express): ");
        String name = sc.nextLine().trim();
        
        int totalSeats = getIntegerInput("Enter Total Seats (default 50): ", 50);
        
        System.out.print("Enter Base Fare Per Km (e.g., 10.00): ");
        double baseFare;
        try {
            String fareInput = sc.nextLine().trim();
            baseFare = fareInput.isEmpty() ? FARE_PER_KM : Double.parseDouble(fareInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid fare. Using default " + FARE_PER_KM);
            baseFare = FARE_PER_KM;
        }

        int tid = internalAddTransport(type, number, name, totalSeats, baseFare);
        if (tid == -1) { System.out.println("Setup aborted due to Transport creation failure."); return; }
        System.out.println("✅ Transport (" + type + " " + number + ") created with ID: " + tid);
        
        // --- STEP 2: ADD ROUTE ---
        System.out.println("\n--- 2. Route Details ---");
        System.out.print("Enter Source Location: ");
        String src = sc.nextLine().trim();
        System.out.print("Enter Destination Location: ");
        String dest = sc.nextLine().trim();

        int routeId = internalAddRoute(tid, src, dest);
        if (routeId == -1) { System.out.println("Setup aborted due to Route creation failure."); return; }
        System.out.println("✅ Route (" + src + " -> " + dest + ") created with ID: " + routeId);

        // --- STEP 3: ADD STATIONS ---
        System.out.println("\n--- 3. Station Details (Stops on the Route) ---");
        System.out.println("Enter station details (type 'done' to finish).");
        System.out.println("Format: name distance(km) arrival(HH:MM) departure(HH:MM)");
        
        List<String[]> stationData = new ArrayList<>();
        int seq = 1;
        while (true) {
            System.out.print("Station " + seq + ": ");
            String line = sc.nextLine().trim();
            if (line.equalsIgnoreCase("done")) break;
            
            String[] parts = line.split("\\s+");
            if (parts.length < 4) {
                System.out.println("Invalid format — required: name distance arrival departure.");
                continue;
            }
            
            try {
                String departure = parts[parts.length - 1];
                String arrival = parts[parts.length - 2];
                double distance = Double.parseDouble(parts[parts.length - 3]);
                String stationName = "";
                for(int i = 0; i < parts.length - 3; i++) {
                    stationName += parts[i] + " ";
                }
                stationData.add(new String[]{stationName.trim(), String.valueOf(distance), arrival, departure});
                seq++;
            } catch (NumberFormatException e) {
                System.out.println("Invalid distance format. Please check input.");
            }
        }

        if (stationData.size() < 2) {
             System.out.println("Setup aborted. At least two stations (start and end) are required.");
             return;
        }
        
        if (!internalAddStations(routeId, stationData)) {
            System.out.println("Setup aborted due to Station creation failure."); 
            return;
        }
        System.out.println("✅ " + stationData.size() + " stations added successfully.");

        // We assume the first and last station created are the start/end points for the whole route.
        // We will retrieve the IDs from the database to be safe.
        int startStationId = -1;
        int endStationId = -1;
        try (Connection con = Jdbc_Connection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT station_id FROM stations WHERE route_id = " + routeId + " ORDER BY sequence_no")) {
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) ids.add(rs.getInt(1));
            
            if (!ids.isEmpty()) {
                startStationId = ids.get(0);
                endStationId = ids.get(ids.size() - 1);
                System.out.println("Using Start Station ID: " + startStationId + " | End Station ID: " + endStationId);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving station IDs. Aborting scheduling.");
            return;
        }


        // --- STEP 4: ADD SCHEDULES ---
        System.out.println("\n--- 4. Schedule Generation ---");
        
        System.out.print("Enter main Departure Time (HH:MM): ");
        String depTimeStr = sc.nextLine().trim();
        System.out.print("Enter main Arrival Time (HH:MM): ");
        String arrTimeStr = sc.nextLine().trim();
        
        
        if (type.equalsIgnoreCase("car")) {
            System.out.print("Enter single travel date (YYYY-MM-DD): ");
            String dateStr = sc.nextLine().trim();
            LocalDate date;
            try {
                 date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                 System.out.println("Invalid date format. Skipping schedule creation.");
                 return;
            }
            internalInsertSingleSchedule(tid, routeId, startStationId, endStationId, depTimeStr, arrTimeStr, date, totalSeats);
        } else {
            // Generate next 60 days
            LocalDate today = LocalDate.now();
            ArrayList<LocalDate> dates = new ArrayList<>();
            for (int i = 0; i < 60; i++) dates.add(today.plusDays(i));
            internalInsertMultipleSchedules(tid, routeId, startStationId, endStationId, depTimeStr, arrTimeStr, dates, totalSeats);
        }

        System.out.println("\n==============================================");
        System.out.println("✅ FULL ROUTE SETUP WIZARD COMPLETE.");
        System.out.println("   Route is now ready for future bookings.");
        System.out.println("==============================================");
    }
    
    // --- MAIN EXECUTION METHOD ---

    public static void main(String[] args) {
        // NOTE: Jdbc_Connection class is assumed to be available for database connection.
        while (true) {
            System.out.println("\n======= SAFAR ADMIN MENU =======");
            System.out.println("1. Run FULL Route Setup Wizard (Streamlined Setup)");
            System.out.println("2. Refresh 60-day Window (Maintenance)");
            System.out.println("3. View Routes & Stations");
            System.out.println("4. SETUP TEST USER (Run Once!)");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int ch = -1;
            try {
                ch = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (ch) {
            case 1:
                Admin_Dashboard_Background_try.runFullSetupWizard();
                break;
            case 2:
                Admin_Dashboard_Background_try.refresh60DayWindow();
                break;
            case 3:
                Admin_Dashboard_Background_try.displayRoutes();
                System.out.print("Enter Route ID to display stations (optional, press Enter): ");
                String input = sc.nextLine().trim();
                if (!input.isEmpty()) {
                    try {
                        int routeId = Integer.parseInt(input);
                        System.out.println("--- STATIONS FOR ROUTE " + routeId + " ---");
                        String sql = "SELECT station_id, station_name, sequence_no, distance_from_source, arrival_time, departure_time FROM stations WHERE route_id = ? ORDER BY sequence_no";
                        try (Connection con = Jdbc_Connection.getConnection();
                            PreparedStatement ps = con.prepareStatement(sql)) {
                           ps.setInt(1, routeId);
                           ResultSet rs = ps.executeQuery();
                           System.out.println("\nStation ID | Name                | Seq | Dist(km) | Arrive   | Depart");
                           System.out.println("-------------------------------------------------------------------");
                           while (rs.next()) {
                               String arr = rs.getString("arrival_time");
                               String dep = rs.getString("departure_time");
                               System.out.printf("%-10d %-20s %-4d %-9.1f %-9s %-9s%n",
                                   rs.getInt("station_id"),
                                   rs.getString("station_name"),
                                   rs.getInt("sequence_no"),
                                   rs.getDouble("distance_from_source"),
                                   arr == null ? "--" : arr.substring(0, 5), 
                                   dep == null ? "--" : dep.substring(0, 5)); 
                           }
                           System.out.println("-------------------------------------------------------------------");
                       } catch (SQLException e) { e.printStackTrace(); }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid Route ID.");
                    }
                }
                break;
            case 4:
                Admin_Dashboard_Background_try.ensureTestUserExists();
                break;
            case 0:
                System.out.println("Exiting.");
                sc.close();
                return; 

            default:
                System.out.println("Invalid choice.");
            }
        }
    }
}}

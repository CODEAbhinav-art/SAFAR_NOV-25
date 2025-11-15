//package safar.gsv;
//import java.sql.*;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Scanner;
//
//public class Admin_Dashboard_Background_try {
//	
//	private static final Scanner sc = new Scanner(System.in);
//    private static final double FARE_PER_KM = 2.5; // changeable
//
//    // 1) Add transport (train/bus/flight/car)
//    public static int addTransport() {
//        System.out.print("Enter Transport Type (train/bus/flight/car): ");
//        String type = sc.nextLine().trim().toLowerCase();
//        System.out.print("Enter Transport Number: ");
//        String number = sc.nextLine().trim();
//        System.out.print("Enter Transport Name (optional): ");
//        String name = sc.nextLine().trim();
//
//        String sql = "INSERT INTO transports (transport_type, transport_no, name) VALUES (?, ?, ?)";
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            ps.setString(1, type);
//            ps.setString(2, number);
//            ps.setString(3, name.isEmpty() ? null : name);
//            ps.executeUpdate();
//            ResultSet rs = ps.getGeneratedKeys();
//            if (rs.next()) {
//                int id = rs.getInt(1);
//                System.out.println("✅ Transport created. ID: " + id);
//                return id;
//            }
//        } catch (SQLException e) { e.printStackTrace(); }
//        return -1;
//    }
//
//    // 2) Show transports
//    public static void displayTransports() {
//        String sql = "SELECT * FROM transports";
//        try (Connection con = Jdbc_Connection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//
//            System.out.println("\nTransport ID | Type   | Number    | Name");
//            System.out.println("-------------------------------------------------");
//            while (rs.next()) {
//                System.out.printf("%-12d %-7s %-10s %-20s%n",
//                    rs.getInt("transport_id"),
//                    rs.getString("transport_type"),
//                    rs.getString("transport_no"),
//                    rs.getString("name"));
//            }
//            System.out.println("-------------------------------------------------");
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    // 3) Add route for a transport
//    public static int addRouteForTransport() {
//        displayTransports();
//        System.out.print("Enter Transport ID to assign route: ");
//        int tid = Integer.parseInt(sc.nextLine().trim());
//
//        System.out.print("Enter Source: ");
//        String src = sc.nextLine().trim();
//        System.out.print("Enter Destination: ");
//        String dest = sc.nextLine().trim();
//
//        String sql = "INSERT INTO routes (transport_id, source, destination) VALUES (?, ?, ?)";
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setInt(1, tid);
//            ps.setString(2, src);
//            ps.setString(3, dest);
//            ps.executeUpdate();
//            ResultSet rs = ps.getGeneratedKeys();
//            if (rs.next()) {
//                int rid = rs.getInt(1);
//                System.out.println("✅ Route added. ID: " + rid);
//                return rid;
//            }
//        } catch (SQLException e) { e.printStackTrace(); }
//        return -1;
//    }
//
//    // 4) Display routes
//    public static void displayRoutes() {
//        String sql = "SELECT r.route_id, t.transport_type, t.transport_no, r.source, r.destination " +
//                     "FROM routes r LEFT JOIN transports t ON r.transport_id = t.transport_id";
//        try (Connection con = Jdbc_Connection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(sql)) {
//            System.out.println("\nRoute ID | Type  | No.     | Source -> Destination");
//            System.out.println("----------------------------------------------------------");
//            while (rs.next()) {
//                System.out.printf("%-8d %-6s %-8s %s -> %s%n",
//                    rs.getInt("route_id"),
//                    rs.getString("transport_type"),
//                    rs.getString("transport_no"),
//                    rs.getString("source"),
//                    rs.getString("destination"));
//            }
//            System.out.println("----------------------------------------------------------");
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    // 5) Add stations for a route (distance + arrival + departure). Uses batch insert.
//    public static void addStationsForRoute() {
//        displayRoutes();
//        System.out.print("Enter Route ID to add stations: ");
//        int routeId = Integer.parseInt(sc.nextLine().trim());
//
//        System.out.println("Enter station details (type 'done' to finish).");
//        System.out.println("Format: station_name distance_from_source_km arrival_time(HH:MM) departure_time(HH:MM)");
//        String sql = "INSERT INTO stations (route_id, station_name, sequence_no, distance_from_source, arrival_time, departure_time) VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//
//            int seq = getNextSequenceForRoute(con, routeId);
//            while (true) {
//                System.out.print("Station " + seq + ": ");
//                String line = sc.nextLine().trim();
//                if (line.equalsIgnoreCase("done")) break;
//                String[] parts = line.split("\\s+");
//                if (parts.length < 4) {
//                    System.out.println("Invalid format — use: name distance arrival departure");
//                    continue;
//                }
//                String name = parts[0];
//                double distance = Double.parseDouble(parts[1]);
//                String arrival = parts[2];
//                String departure = parts[3];
//
//                ps.setInt(1, routeId);
//                ps.setString(2, name);
//                ps.setInt(3, seq++);
//                ps.setDouble(4, distance);
//                ps.setTime(5, parseTimeOrNull(arrival));
//                ps.setTime(6, parseTimeOrNull(departure));
//                ps.addBatch();
//            }
//            ps.executeBatch();
//            System.out.println("✅ Stations added.");
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    private static int getNextSequenceForRoute(Connection con, int routeId) throws SQLException {
//        String q = "SELECT MAX(sequence_no) as mx FROM stations WHERE route_id = ?";
//        try (PreparedStatement ps = con.prepareStatement(q)) {
//            ps.setInt(1, routeId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                int mx = rs.getInt("mx");
//                return mx + 1;
//            }
//        }
//        return 1;
//    }
//
//    // 6) Display stations of a route
//    public static void displayStationsForRoute(int routeId) {
//        String sql = "SELECT station_id, station_name, sequence_no, distance_from_source, arrival_time, departure_time " +
//                     "FROM stations WHERE route_id = ? ORDER BY sequence_no";
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, routeId);
//            ResultSet rs = ps.executeQuery();
//            System.out.println("\nStation ID | Name                | Seq | Dist(km) | Arrive   | Depart");
//            System.out.println("-------------------------------------------------------------------");
//            while (rs.next()) {
//                String arr = rs.getString("arrival_time");
//                String dep = rs.getString("departure_time");
//                System.out.printf("%-10d %-20s %-4d %-9.1f %-9s %-9s%n",
//                    rs.getInt("station_id"),
//                    rs.getString("station_name"),
//                    rs.getInt("sequence_no"),
//                    rs.getDouble("distance_from_source"),
//                    arr == null ? "--" : arr,
//                    dep == null ? "--" : dep);
//            }
//            System.out.println("-------------------------------------------------------------------");
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    // 7) Add schedule: if car -> ask for date; else -> auto generate next 60 days
//    public static void addScheduleForTransport() {
//        displayTransports();
//        System.out.print("Choose Transport ID: ");
//        int tid = Integer.parseInt(sc.nextLine().trim());
//
//        // fetch transport info
//        String tQuery = "SELECT transport_type, transport_no FROM transports WHERE transport_id = ?";
//        String transportType = null;
//        String transportNo = null;
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement tp = con.prepareStatement(tQuery)) {
//            tp.setInt(1, tid);
//            ResultSet trs = tp.executeQuery();
//            if (trs.next()) {
//                transportType = trs.getString("transport_type");
//                transportNo = trs.getString("transport_no");
//            } else {
//                System.out.println("Transport not found.");
//                return;
//            }
//        } catch (SQLException e) { e.printStackTrace(); return; }
//
//        // show routes for this transport
//        String rQuery = "SELECT route_id, source, destination FROM routes WHERE transport_id = ?";
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement pr = con.prepareStatement(rQuery)) {
//            pr.setInt(1, tid);
//            ResultSet rr = pr.executeQuery();
//            System.out.println("\nRoutes for transport:");
//            while (rr.next()) {
//                System.out.printf("%d : %s -> %s%n", rr.getInt("route_id"), rr.getString("source"), rr.getString("destination"));
//            }
//        } catch (SQLException e) { e.printStackTrace(); return; }
//
//        System.out.print("Enter Route ID to schedule: ");
//        int routeId = Integer.parseInt(sc.nextLine().trim());
//
//        // show stations for user convenience
//        displayStationsForRoute(routeId);
//
//        System.out.print("Enter Start Station ID: ");
//        int startStation = Integer.parseInt(sc.nextLine().trim());
//        System.out.print("Enter End Station ID: ");
//        int endStation = Integer.parseInt(sc.nextLine().trim());
//
//        System.out.print("Enter departure time from start (HH:MM) : ");
//        String depTimeStr = sc.nextLine().trim();
//        System.out.print("Enter arrival time at end (HH:MM) : ");
//        String arrTimeStr = sc.nextLine().trim();
//
//        System.out.print("Enter total seats: ");
//        int totalSeats = Integer.parseInt(sc.nextLine().trim());
//
//        if (transportType.equalsIgnoreCase("car")) {
//            System.out.print("Enter travel date (YYYY-MM-DD): ");
//            String dateStr = sc.nextLine().trim();
//            LocalDate date = LocalDate.parse(dateStr);
//            insertSingleSchedule(tid, routeId, transportType, transportNo, startStation, endStation, depTimeStr, arrTimeStr, date, totalSeats);
//            System.out.println("✅ Car schedule added for " + date);
//        } else {
//            // generate next 60 days
//            LocalDate today = LocalDate.now();
//            ArrayList<LocalDate> dates = new ArrayList<>();
//            for (int i = 0; i < 60; i++) dates.add(today.plusDays(i));
//            insertMultipleSchedules(tid, routeId, transportType, transportNo, startStation, endStation, depTimeStr, arrTimeStr, dates, totalSeats);
//            System.out.println("✅ 60-day schedules generated for transport " + transportNo);
//        }
//    }
//
//    private static void insertSingleSchedule(int tid, int routeId, String transportType, String transportNo,
//                                             int startStation, int endStation, String depTimeStr, String arrTimeStr,
//                                             LocalDate date, int totalSeats) {
//        String sql = "INSERT INTO schedules (transport_id, route_id, transport_type, transport_no, start_station_id, end_station_id, departure_time, arrival_time, date, total_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            ps.setInt(1, tid);
//            ps.setInt(2, routeId);
//            ps.setString(3, transportType);
//            ps.setString(4, transportNo);
//            ps.setInt(5, startStation);
//            ps.setInt(6, endStation);
//            ps.setTime(7, parseTimeOrNull(depTimeStr));
//            ps.setTime(8, parseTimeOrNull(arrTimeStr));
//            ps.setDate(9, Date.valueOf(date));
//            ps.setInt(10, totalSeats);
//            ps.executeUpdate();
//
//            // generate segments for this new schedule only
//            try (ResultSet rs = ps.getGeneratedKeys()) {
//                if (rs.next()) {
//                    int scheduleId = rs.getInt(1);
//                    generateSegmentsForSingleSchedule(con, routeId, scheduleId, date);
//                }
//            }
//
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    private static void insertMultipleSchedules(int tid, int routeId, String transportType, String transportNo,
//                                                int startStation, int endStation, String depTimeStr, String arrTimeStr,
//                                                List<LocalDate> dates, int totalSeats) {
//        String sql = "INSERT INTO schedules (transport_id, route_id, transport_type, transport_no, start_station_id, end_station_id, departure_time, arrival_time, date, total_seats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (Connection con = Jdbc_Connection.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//            for (LocalDate d : dates) {
//                ps.setInt(1, tid);
//                ps.setInt(2, routeId);
//                ps.setString(3, transportType);
//                ps.setString(4, transportNo);
//                ps.setInt(5, startStation);
//                ps.setInt(6, endStation);
//                ps.setTime(7, parseTimeOrNull(depTimeStr));
//                ps.setTime(8, parseTimeOrNull(arrTimeStr));
//                ps.setDate(9, Date.valueOf(d));
//                ps.setInt(10, totalSeats);
//                ps.addBatch();
//            }
//            ps.executeBatch();
//
//            // collect generated schedule ids
//            List<Integer> newScheduleIds = new ArrayList<>();
//            try (ResultSet rs = ps.getGeneratedKeys()) {
//                while (rs.next()) newScheduleIds.add(rs.getInt(1));
//            }
//
//            // generate segments for each new schedule
//            for (int scheduleId : newScheduleIds) {
//                generateSegmentsForSingleSchedule(con, routeId, scheduleId, null); // null date => will use station day offsets only
//            }
//
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    // Generate segments for a single schedule (uses station times + detects day offsets)
//    private static void generateSegmentsForSingleSchedule(Connection con, int routeId, int scheduleId, LocalDate scheduleDate) throws SQLException {
//        // get stations in order with times & distances
//        String getStationsSql = "SELECT station_id, distance_from_source, arrival_time, departure_time FROM stations WHERE route_id = ? ORDER BY sequence_no";
//        List<Integer> stationIds = new ArrayList<>();
//        List<Double> distances = new ArrayList<>();
//        List<Time> arrivals = new ArrayList<>();
//        List<Time> departures = new ArrayList<>();
//
//        try (PreparedStatement ps = con.prepareStatement(getStationsSql)) {
//            ps.setInt(1, routeId);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                stationIds.add(rs.getInt("station_id"));
//                distances.add(rs.getDouble("distance_from_source"));
//                arrivals.add(rs.getTime("arrival_time"));
//                departures.add(rs.getTime("departure_time"));
//            }
//        }
//
//        if (stationIds.size() < 2) return; // nothing to generate
//
//        // compute day offsets for stations based on departure/arrival times relative ordering
//        List<Integer> dayOffsets = computeDayOffsets(arrivals, departures);
//
//        // prepare insert for segments
//        String insertSeg = "INSERT INTO schedule_segments (schedule_id, start_station_id, end_station_id, distance_km, fare, seats_available, departure_time, departure_day_offset, arrival_time, arrival_day_offset) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement ps = con.prepareStatement(insertSeg)) {
//            for (int i = 0; i < stationIds.size() - 1; i++) {
//                for (int j = i + 1; j < stationIds.size(); j++) {
//                    int start = stationIds.get(i);
//                    int end = stationIds.get(j);
//                    double dist = distances.get(j) - distances.get(i);
//                    double fare = dist * FARE_PER_KM;
//                    Time depTime = departures.get(i) != null ? departures.get(i) : arrivals.get(i); // fallback
//                    Time arrTime = arrivals.get(j) != null ? arrivals.get(j) : departures.get(j);
//                    int depOffset = dayOffsets.get(i);
//                    int arrOffset = dayOffsets.get(j);
//
//                    ps.setInt(1, scheduleId);
//                    ps.setInt(2, start);
//                    ps.setInt(3, end);
//                    ps.setDouble(4, dist);
//                    ps.setDouble(5, fare);
//                    ps.setInt(6, 100); // default seats
//                    ps.setTime(7, depTime);
//                    ps.setInt(8, depOffset);
//                    ps.setTime(9, arrTime);
//                    ps.setInt(10, arrOffset);
//                    ps.addBatch();
//                }
//            }
//            ps.executeBatch();
//        }
//    }
//
//    // compute day offsets: start offset 0, increment offset when time decreases
//    private static List<Integer> computeDayOffsets(List<Time> arrivals, List<Time> departures) {
//        List<Integer> offsets = new ArrayList<>();
//        int offset = 0;
//        Time prevTime = null;
//
//        // traverse stations in order; choose the earlier of departure/arrival present for comparison
//        for (int i = 0; i < Math.max(arrivals.size(), departures.size()); i++) {
//            Time t = departures.get(i) != null ? departures.get(i) : arrivals.get(i);
//            if (t == null) {
//                // if both null, keep same offset
//                offsets.add(offset);
//                continue;
//            }
//            if (prevTime != null && t.before(prevTime)) {
//                offset++;
//            }
//            offsets.add(offset);
//            prevTime = t;
//        }
//        return offsets;
//    }
//
//    // parse time helper
//    private static Time parseTimeOrNull(String s) {
//        if (s == null || s.isEmpty()) return null;
//        try {
//            // accept HH:MM or HH:MM:SS
//            if (s.length() == 5) s = s + ":00";
//            return Time.valueOf(s);
//        } catch (Exception e) {
//            System.out.println("⚠️ Invalid time format '" + s + "'. Expected HH:MM or HH:MM:SS. Using null.");
//            return null;
//        }
//    }
//
//    // 8) Auto-refresh schedules: maintain rolling 60-day window for non-car transports
//    public static void refresh60DayWindow() {
//        System.out.println("Refreshing 60-day windows...");
//        String groupsSql = "SELECT DISTINCT route_id, transport_no, transport_type FROM schedules WHERE transport_type <> 'car'";
//        try (Connection con = Jdbc_Connection.getConnection();
//             Statement st = con.createStatement();
//             ResultSet rs = st.executeQuery(groupsSql)) {
//
//            while (rs.next()) {
//                int routeId = rs.getInt("route_id");
//                String transportNo = rs.getString("transport_no");
//                String transportType = rs.getString("transport_type");
//
//                // find max date for this group
//                String maxSql = "SELECT MAX(date) AS mx FROM schedules WHERE route_id = ? AND transport_no = ?";
//                LocalDate maxDate = null;
//                try (PreparedStatement ps = con.prepareStatement(maxSql)) {
//                    ps.setInt(1, routeId);
//                    ps.setString(2, transportNo);
//                    ResultSet r2 = ps.executeQuery();
//                    if (r2.next()) {
//                        Date d = r2.getDate("mx");
//                        if (d != null) maxDate = d.toLocalDate();
//                    }
//                }
//
//                LocalDate today = LocalDate.now();
//                LocalDate target = today.plusDays(59); // we want schedules up to today+59 inclusive
//                if (maxDate == null) {
//                    // nothing exists, generate 60 days from today
//                    List<LocalDate> dates = new ArrayList<>();
//                    for (int i = 0; i < 60; i++) dates.add(today.plusDays(i));
//                    ensureSchedulesForGroup(con, routeId, transportNo, dates);
//                } else {
//                    while (maxDate.isBefore(target)) {
//                        // add next day
//                        LocalDate next = maxDate.plusDays(1);
//                        ensureSchedulesForGroup(con, routeId, transportNo, Collections.singletonList(next));
//                        maxDate = next;
//                    }
//                }
//            }
//            System.out.println("Refresh complete.");
//        } catch (SQLException e) { e.printStackTrace(); }
//    }
//
//    // helper: add schedules for a group (route+transport_no) if not there already
//    private static void ensureSchedulesForGroup(Connection con, int routeId, String transportNo, List<LocalDate> dates) throws SQLException {
//        // find some transport_id/transport_type for this route+transport_no (we assume same transport_no used)
//        String q = "SELECT transport_id, transport_type FROM schedules WHERE route_id = ? AND transport_no = ? LIMIT 1";
//        Integer tid = null;
//        String ttype = null;
//        try (PreparedStatement ps = con.prepareStatement(q)) {
//            ps.setInt(1, routeId);
//            ps.setString(2, transportNo);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                tid = rs.getInt("transport_id");
//                ttype = rs.getString("transport_type");
//            } else {
//                // fallback: try transports table
//                String q2 = "SELECT transport_id, transport_type FROM transports WHERE transport_no = ? LIMIT 1";
//                try (PreparedStatement p2 = con.prepareStatement(q2)) {
//                    p2.setString(1, transportNo);
//                    ResultSet r2 = p2.executeQuery();
//                    if (r2.next()) {
//                        tid = r2.getInt("transport_id");
//                        ttype = r2.getString("transport_type");
//                    }
//                }
//            }
//        }
//        if (tid == null) return;
//
//        String insert = "INSERT IGNORE INTO schedules (transport_id, route_id, transport_type, transport_no, date, total_seats) VALUES (?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement ps = con.prepareStatement(insert)) {
//            for (LocalDate d : dates) {
//                ps.setInt(1, tid);
//                ps.setInt(2, routeId);
//                ps.setString(3, ttype);
//                ps.setString(4, transportNo);
//                ps.setDate(5, Date.valueOf(d));
//                ps.setInt(6, 100);
//                ps.addBatch();
//            }
//            ps.executeBatch();
//        }
//    }
//
//}


package safar.gsv;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Admin_Dashboard_Background_try {

	private static final Scanner sc = new Scanner(System.in);
    private static final double FARE_PER_KM = 2.5; // changeable

    // --- Helper Functions ---

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
    
    private static List<Integer> computeDayOffsets(List<Time> arrivals, List<Time> departures) {
        List<Integer> offsets = new ArrayList<>();
        int offset = 0;
        Time prevTime = null;

        for (int i = 0; i < Math.max(arrivals.size(), departures.size()); i++) {
            Time t = departures.get(i) != null ? departures.get(i) : arrivals.get(i);
            
            if (t == null) {
                offsets.add(offset);
                continue;
            }
            
            if (prevTime != null && t.before(prevTime)) {
                offset++;
            }
            
            offsets.add(offset);
            prevTime = t;
        }
        return offsets;
    }


    // --- Core Admin Functionality ---

    // 1) Add transport (train/bus/flight/car)
    public static int addTransport() {
        System.out.print("Enter Transport Type (train/bus/flight/car): ");
        String type = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter Transport Number: ");
        String number = sc.nextLine().trim();
        System.out.print("Enter Transport Name (optional): ");
        String name = sc.nextLine().trim();
        System.out.print("Enter Total Seats: ");
        int totalSeats;
        try {
            totalSeats = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid seat count. Using default 100.");
            totalSeats = 100;
        }
        System.out.print("Enter Base Fare Per Km (e.g., 2.5): ");
        double baseFare;
        try {
            baseFare = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid fare. Using default " + FARE_PER_KM);
            baseFare = FARE_PER_KM;
        }


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
                int id = rs.getInt(1);
                System.out.println("✅ Transport created. ID: " + id);
                return id;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return -1;
    }

    // 2) Show transports
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

    // 3) Add route for a transport
    public static int addRouteForTransport() {
        displayTransports();
        System.out.print("Enter Transport ID to assign route: ");
        int tid = -1;
        try {
            tid = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Transport ID.");
            return -1;
        }

        System.out.print("Enter Source: ");
        String src = sc.nextLine().trim();
        System.out.print("Enter Destination: ");
        String dest = sc.nextLine().trim();

        String sql = "INSERT INTO routes (transport_id, source, destination) VALUES (?, ?, ?)";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, tid);
            ps.setString(2, src);
            ps.setString(3, dest);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int rid = rs.getInt(1);
                System.out.println("✅ Route added. ID: " + rid);
                return rid;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("⚠️ ERROR: A route with the same Source, Destination, and Transport ID already exists (Unique Constraint Violation) or Transport ID is invalid.");
        } catch (SQLException e) { 
             e.printStackTrace(); 
        }
        return -1;
    }

    // 4) Display routes
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

    // 5) Add stations for a route (distance + arrival + departure). Uses batch insert.
    public static void addStationsForRoute() {
        displayRoutes();
        System.out.print("Enter Route ID to add stations: ");
        int routeId = -1;
        try {
            routeId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Route ID.");
            return;
        }

        System.out.println("Enter station details (type 'done' to finish).");
        System.out.println("Format: station_name distance_from_source_km arrival_time(HH:MM) departure_time(HH:MM)");
        String sql = "INSERT INTO stations (route_id, station_name, sequence_no, distance_from_source, arrival_time, departure_time) VALUES (?, ?, ?, ?, ?, ?)";

        Connection con = null; 

        try {
            con = Jdbc_Connection.getConnection();
            con.setAutoCommit(false); 

            try (PreparedStatement ps = con.prepareStatement(sql)) { 
                int seq = getNextSequenceForRoute(con, routeId);
                int batchCount = 0;
                
                while (true) {
                    System.out.print("Station " + seq + ": ");
                    String line = sc.nextLine().trim();
                    if (line.equalsIgnoreCase("done")) break;
                    
                    String[] parts = line.split("\\s+");
                    if (parts.length < 4) {
                        System.out.println("Invalid format — use: name distance arrival departure");
                        continue;
                    }
                    
                    String name = "";
                    double distance;
                    String arrival;
                    String departure;
                    
                    try {
                        departure = parts[parts.length - 1];
                        arrival = parts[parts.length - 2];
                        distance = Double.parseDouble(parts[parts.length - 3]);
                        
                        for(int i = 0; i < parts.length - 3; i++) {
                            name += parts[i] + " ";
                        }
                        name = name.trim();

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid distance format.");
                        continue;
                    }

                    ps.setInt(1, routeId);
                    ps.setString(2, name);
                    ps.setInt(3, seq++);
                    ps.setDouble(4, distance);
                    ps.setTime(5, parseTimeOrNull(arrival));
                    ps.setTime(6, parseTimeOrNull(departure));
                    ps.addBatch();
                    batchCount++;
                }
                
                if (batchCount > 0) {
                    ps.executeBatch();
                    con.commit();
                    System.out.println("✅ Stations added.");
                } else {
                    con.rollback(); 
                    System.out.println("No stations added. Rollback executed.");
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
            if (con != null) {
                try {
                    System.out.println("❌ Transaction failed. Performing rollback.");
                    con.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // 6) Display stations of a route
    public static void displayStationsForRoute(int routeId) {
        String sql = "SELECT station_id, station_name, sequence_no, distance_from_source, arrival_time, departure_time " +
                     "FROM stations WHERE route_id = ? ORDER BY sequence_no";
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, routeId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\nStation ID | Name                | Seq | Dist(km) | Arrive   | Depart");
            System.out.println("-------------------------------------------------------------------");
            boolean found = false;
            while (rs.next()) {
                found = true;
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
            if (!found) {
                System.out.println("No stations found for Route ID " + routeId);
            }
            System.out.println("-------------------------------------------------------------------");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 7) Add schedule
    public static void addScheduleForTransport() {
        displayTransports();
        System.out.print("Choose Transport ID: ");
        int tid = -1;
        try {
            tid = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Transport ID.");
            return;
        }


        String tQuery = "SELECT transport_type, transport_no, total_seats FROM transports WHERE transport_id = ?";
        String transportType = null;
        String transportNo = null;
        int transportSeats = 0;
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement tp = con.prepareStatement(tQuery)) {
            tp.setInt(1, tid);
            ResultSet trs = tp.executeQuery();
            if (trs.next()) {
                transportType = trs.getString("transport_type");
                transportNo = trs.getString("transport_no");
                transportSeats = trs.getInt("total_seats"); 
            } else {
                System.out.println("Transport not found.");
                return;
            }
        } catch (SQLException e) { e.printStackTrace(); return; }

        String rQuery = "SELECT route_id, source, destination FROM routes WHERE transport_id = ?";
        int selectedRouteId = -1;
        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement pr = con.prepareStatement(rQuery)) {
            pr.setInt(1, tid);
            ResultSet rr = pr.executeQuery();
            System.out.println("\nRoutes for transport (" + transportType + " " + transportNo + "):");
            boolean found = false;
            while (rr.next()) {
                found = true;
                System.out.printf("%d : %s -> %s%n", rr.getInt("route_id"), rr.getString("source"), rr.getString("destination"));
            }
            if (!found) {
                 System.out.println("No routes defined for this transport.");
                 return;
            }
        } catch (SQLException e) { e.printStackTrace(); return; }

        System.out.print("Enter Route ID to schedule: ");
        int routeId = Integer.parseInt(sc.nextLine().trim());
        selectedRouteId = routeId;

        displayStationsForRoute(selectedRouteId);

        System.out.print("Enter Start Station ID: ");
        int startStation = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter End Station ID: ");
        int endStation = Integer.parseInt(sc.nextLine().trim());

        System.out.print("Enter departure time from start (HH:MM) : ");
        String depTimeStr = sc.nextLine().trim();
        System.out.print("Enter arrival time at end (HH:MM) : ");
        String arrTimeStr = sc.nextLine().trim();
        
        System.out.print("Enter total seats (default " + transportSeats + "): ");
        String seatsInput = sc.nextLine().trim();
        int totalSeats = transportSeats;
        if (!seatsInput.isEmpty()) {
            try {
                totalSeats = Integer.parseInt(seatsInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid seat input. Using default " + transportSeats);
            }
        }
        
        if (totalSeats < 1) {
            System.out.println("Total seats must be at least 1.");
            return;
        }


        if (transportType.equalsIgnoreCase("car")) {
            System.out.print("Enter travel date (YYYY-MM-DD): ");
            String dateStr = sc.nextLine().trim();
            LocalDate date;
            try {
                 date = LocalDate.parse(dateStr);
            } catch (Exception e) {
                 System.out.println("Invalid date format. Skipping schedule creation.");
                 return;
            }

            insertSingleSchedule(tid, routeId, startStation, endStation, depTimeStr, arrTimeStr, date, totalSeats);
            System.out.println("✅ Car schedule added for " + date);
        } else {
            LocalDate today = LocalDate.now();
            ArrayList<LocalDate> dates = new ArrayList<>();
            for (int i = 0; i < 60; i++) dates.add(today.plusDays(i));
            insertMultipleSchedules(tid, routeId, startStation, endStation, depTimeStr, arrTimeStr, dates, totalSeats);
            System.out.println("✅ 60-day schedules generated for transport " + transportNo);
        }
    }

    // FIX: Removed transportType and transportNo from parameters and SQL
    private static void insertSingleSchedule(int tid, int routeId,
                                             int startStation, int endStation, String depTimeStr, String arrTimeStr,
                                             LocalDate date, int totalSeats) {
        // FIX: Removed transport_type and transport_no from INSERT statement
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

        } catch (SQLException e) { e.printStackTrace(); }
    }

    // FIX: Removed transportType and transportNo from parameters and SQL
    private static void insertMultipleSchedules(int tid, int routeId,
                                                int startStation, int endStation, String depTimeStr, String arrTimeStr,
                                                List<LocalDate> dates, int totalSeats) {
        // FIX: Removed transport_type and transport_no from INSERT statement
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
            
        } catch (SQLException e) { 
            e.printStackTrace(); 
            if (con != null) {
                try {
                    System.out.println("❌ Transaction failed. Performing rollback.");
                    con.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

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

        // List<Integer> dayOffsets = computeDayOffsets(arrivals, departures);

        String insertSeg = "INSERT INTO schedule_segments (schedule_id, start_station_id, end_station_id, distance_km, fare, seats_available) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = con.prepareStatement(insertSeg)) {
            // Note: Con passed in may or may not be in autocommit=false mode. Assuming autocommit is managed by caller.
            // If called from refresh, con is set to autocommit=true outside.
            // If called from insertMultipleSchedules, it's inside a transaction.
            // Let's ensure segment generation is always atomic:
            boolean originalAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            
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
             throw e; 
        }
    }


    // 8) Auto-refresh schedules
    public static void refresh60DayWindow() {
        System.out.println("Refreshing 60-day windows...");
        // FIX: Changed WHERE clause to filter by t.transport_type, as transport_type was dropped from 'schedules'
        String groupsSql = "SELECT DISTINCT s.route_id, s.transport_id, t.transport_no, t.transport_type, t.total_seats, s.start_station_id, s.end_station_id, s.departure_time, s.arrival_time FROM schedules s JOIN transports t ON s.transport_id = t.transport_id WHERE t.transport_type <> 'car' GROUP BY s.route_id, s.transport_id, t.transport_no, t.transport_type, t.total_seats, s.start_station_id, s.end_station_id, s.departure_time, s.arrival_time";
        
        try (Connection con = Jdbc_Connection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(groupsSql)) {

            while (rs.next()) {
                int routeId = rs.getInt("route_id");
                int transportId = rs.getInt("transport_id");
                String transportNo = rs.getString("transport_no");
                String transportType = rs.getString("transport_type");
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

    // FIX: Removed transportNo and transportType from parameters and SQL
    private static void ensureSchedulesForGroup(Connection con, int routeId, int tid, 
                                                int startStId, int endStId, Time depTime, Time arrTime, 
                                                int totalSeats, List<LocalDate> dates) throws SQLException {

        // FIX: Removed transport_type and transport_no from INSERT statement
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


    // --- Main Method for Execution Flow ---
    
    public static void main(String[] args) {
        while (true) {
            System.out.println("\n======= SAFAR ADMIN MENU =======");
            System.out.println("1. Run FULL Setup Wizard (Transport -> Route -> Stations -> Schedule)");
            System.out.println("2. Exit");
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
                System.out.println("\n--- 1. ADD TRANSPORT ---");
                Admin_Dashboard_Background_try.addTransport();
                
                System.out.println("\n--- 2. ADD ROUTE ---");
                Admin_Dashboard_Background_try.displayTransports();
                int routeId = Admin_Dashboard_Background_try.addRouteForTransport();

                if (routeId == -1) {
                    System.out.println("Cannot proceed with setup. Route creation failed.");
                    break;
                }
                
                System.out.println("\n--- 3. ADD STATIONS ---");
                Admin_Dashboard_Background_try.displayRoutes();
                Admin_Dashboard_Background_try.addStationsForRoute(); 

                System.out.println("\n--- 4. VIEW STATIONS ---");
                Admin_Dashboard_Background_try.displayRoutes();
                System.out.print("Enter Route ID to display stations (e.g., " + routeId + "): ");
                try {
                     int rid = Integer.parseInt(sc.nextLine().trim());
                     Admin_Dashboard_Background_try.displayStationsForRoute(rid);
                } catch (NumberFormatException e) {
                     System.out.println("Invalid Route ID entered. Skipping station display.");
                }
               
                System.out.println("\n--- 5. ADD SCHEDULE & GENERATE SEGMENTS ---");
                Admin_Dashboard_Background_try.addScheduleForTransport();
                
                System.out.println("\n--- 6. REFRESH 60-DAY WINDOW ---");
                Admin_Dashboard_Background_try.refresh60DayWindow();
                
                System.out.println("\n==============================================");
                System.out.println("✅ FULL SETUP WIZARD COMPLETE.");
                System.out.println("==============================================");
                
                break;

            case 2:
                System.out.println("Exiting.");
                sc.close();
                return; 

            default:
                System.out.println("Invalid choice.");
            }
        }
    }
}
package safar.gsv;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class User_Dashboard_Background {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter your user_id: ");
        String userId = sc.nextLine().trim();

        // simple loop for user actions
        while (true) {
            System.out.println("\n======== SAFAR USER MENU ========");
            System.out.println("1. Search Available Routes");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Booking History");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            String choiceLine = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(choiceLine);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    searchAvailableRoutes();
                    break;
                case 2:
                    bookTicket(userId);
                    break;
                case 3:
                    cancelBooking(userId);
                    break;
                case 4:
                    viewBookingHistory(userId);
                    break;
                case 5:
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // -------------------------
    // 1) Search schedules by source/destination/date
    // -------------------------
    private static void searchAvailableRoutes() {
        System.out.print("Enter Source Station (exact name): ");
        String source = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter Destination Station (exact name): ");
        String destination = sc.nextLine().trim().toLowerCase();
        System.out.print("Enter Travel Date (yyyy-mm-dd): ");
        String dateStr = sc.nextLine().trim();

        Date travelDate;
        try {
            LocalDate ld = LocalDate.parse(dateStr);
            travelDate = Date.valueOf(ld);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }

        String sql =
        	    "SELECT s.schedule_id, r.route_id, t.transport_id, t.transport_type, t.transport_no, t.name, " +
        	    "       s.departure_time, s.arrival_time, s.date, s.total_seats " +
        	    "FROM schedules s " +
        	    "JOIN routes r ON s.route_id = r.route_id " +
        	    "JOIN transports t ON r.transport_id = t.transport_id " +
        	    "JOIN stations src ON s.start_station_id = src.station_id " +
        	    "JOIN stations dst ON s.end_station_id = dst.station_id " +
        	    "WHERE r.source = ? AND r.destination = ? AND s.date = ? " +
        	    "ORDER BY t.transport_type, s.departure_time";


        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, source);
            ps.setString(2, destination);
            ps.setDate(3, travelDate);

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                System.out.println();
                System.out.printf("%-10s %-8s %-10s %-20s %-10s %-10s %-8s%n",
                        "ScheduleID", "Type", "Number", "Name", "DepTime", "ArrTime", "Date");
                System.out.println("--------------------------------------------------------------------------------");
                while (rs.next()) {
                    found = true;
                    int schedId = rs.getInt("schedule_id");
                    String ttype = rs.getString("transport_type");
                    String tno = rs.getString("transport_no");
                    String tname = rs.getString("name");
                    Time dep = rs.getTime("departure_time");
                    Time arr = rs.getTime("arrival_time");
                    Date dt = rs.getDate("date");

                    System.out.printf("%-10d %-8s %-10s %-20s %-10s %-10s %-8s%n",
                            schedId,
                            safeString(ttype),
                            safeString(tno),
                            safeString(tname),
                            dep == null ? "--" : dep.toString(),
                            arr == null ? "--" : arr.toString(),
                            dt == null ? "--" : dt.toString()
                    );
                }
                if (!found) System.out.println("No schedules found for given route & date.");
            }

        } catch (SQLException e) {
            System.out.println("SQL error while searching schedules:");
            e.printStackTrace();
        }
    }

    // -------------------------
    // 2) Book ticket (choose schedule -> choose segment -> choose seats)
    // -------------------------
    private static void bookTicket(String userId) {
        System.out.print("Enter Schedule ID to book: ");
        int scheduleId;
        try {
            scheduleId = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid schedule id.");
            return;
        }

        // list segments for the schedule
        String segQuery =
            "SELECT ss.segment_id, ss.start_station_id, ss.end_station_id, st1.station_name AS from_name, " +
            "       st2.station_name AS to_name, ss.distance_km, ss.fare, ss.seats_available " +
            "FROM schedule_segments ss " +
            "JOIN stations st1 ON ss.start_station_id = st1.station_id " +
            "JOIN stations st2 ON ss.end_station_id = st2.station_id " +
            "WHERE ss.schedule_id = ? " +
            "ORDER BY st1.sequence_no, st2.sequence_no";

        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(segQuery)) {

            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                System.out.println();
                System.out.printf("%-10s %-20s %-20s %-8s %-10s %-8s%n",
                        "SegmentID", "From", "To", "Distance", "Fare", "Seats");
                System.out.println("---------------------------------------------------------------------------");
                while (rs.next()) {
                    any = true;
                    int segId = rs.getInt("segment_id");
                    String from = rs.getString("from_name");
                    String to = rs.getString("to_name");
                    double dist = rs.getDouble("distance_km");
                    double fare = rs.getDouble("fare");
                    int seats = rs.getInt("seats_available");
                    System.out.printf("%-10d %-20s %-20s %-8.1f %-10.2f %-8d%n",
                            segId, safeString(from), safeString(to), dist, fare, seats);
                }
                if (!any) {
                    System.out.println("No segments available for this schedule.");
                    return;
                }
            }

            System.out.print("\nEnter Segment ID to book: ");
            int segmentId = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter number of seats to book: ");
            int seatsToBook = Integer.parseInt(sc.nextLine().trim());
            if (seatsToBook <= 0) {
                System.out.println("Number of seats must be positive.");
                return;
            }

            // check availability
            String checkSql = "SELECT seats_available FROM schedule_segments WHERE segment_id = ? FOR UPDATE";
            String updateSegSql = "UPDATE schedule_segments SET seats_available = seats_available - ? WHERE segment_id = ?";
            String insertBookingSql = "INSERT INTO bookings (user_id, schedule_id, segment_id, num_seats, status) VALUES (?, ?, ?, ?, 'booked')";

            // use transaction
            try {
                con.setAutoCommit(false);

                int seatsAvail;
                try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                    checkPs.setInt(1, segmentId);
                    try (ResultSet crs = checkPs.executeQuery()) {
                        if (!crs.next()) {
                            System.out.println("Segment not found.");
                            con.rollback();
                            return;
                        }
                        seatsAvail = crs.getInt("seats_available");
                    }
                }

                if (seatsAvail < seatsToBook) {
                    System.out.println("Not enough seats available. Available: " + seatsAvail);
                    con.rollback();
                    return;
                }

                // update seats
                try (PreparedStatement upd = con.prepareStatement(updateSegSql)) {
                    upd.setInt(1, seatsToBook);
                    upd.setInt(2, segmentId);
                    upd.executeUpdate();
                }

                // insert booking
                try (PreparedStatement ins = con.prepareStatement(insertBookingSql, Statement.RETURN_GENERATED_KEYS)) {
                    ins.setString(1, userId);
                    ins.setInt(2, scheduleId);
                    ins.setInt(3, segmentId);
                    ins.setInt(4, seatsToBook);
                    ins.executeUpdate();

                    try (ResultSet gk = ins.getGeneratedKeys()) {
                        if (gk.next()) {
                            int bookingId = gk.getInt(1);
                            System.out.println("✅ Booking successful. Booking ID: " + bookingId);
                        } else {
                            System.out.println("✅ Booking successful.");
                        }
                    }
                }

                con.commit();
            } catch (SQLException transEx) {
                try { con.rollback(); } catch (SQLException rbe) { /* ignore */ }
                throw transEx;
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
            }

        } catch (SQLException e) {
            System.out.println("SQL error during booking:");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input.");
        }
    }

    // -------------------------
    // 3) Cancel booking (restore seats)
    // -------------------------
    private static void cancelBooking(String userId) {
        // show active bookings for user
        String listSql =
            "SELECT b.booking_id, b.schedule_id, b.segment_id, b.num_seats, b.booking_date, b.status, " +
            "       st1.station_name AS from_name, st2.station_name AS to_name, ss.fare, t.name AS transport_name, s.date " +
            "FROM bookings b " +
            "JOIN schedule_segments ss ON b.segment_id = ss.segment_id " +
            "JOIN schedules s ON b.schedule_id = s.schedule_id " +
            "JOIN stations st1 ON ss.start_station_id = st1.station_id " +
            "JOIN stations st2 ON ss.end_station_id = st2.station_id " +
            "LEFT JOIN transports t ON s.transport_id = t.transport_id " +
            "WHERE b.user_id = ? AND b.status = 'booked' " +
            "ORDER BY b.booking_date";

        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(listSql)) {

            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                System.out.println();
                System.out.printf("%-8s %-8s %-8s %-20s %-20s %-6s %-20s%n",
                        "BookID", "SchedID", "SegID", "From", "To", "Seats", "BookingDate");
                System.out.println("---------------------------------------------------------------------------------------");
                while (rs.next()) {
                    any = true;
                    int bid = rs.getInt("booking_id");
                    int sid = rs.getInt("schedule_id");
                    int seg = rs.getInt("segment_id");
                    String from = rs.getString("from_name");
                    String to = rs.getString("to_name");
                    int seats = rs.getInt("num_seats");
                    Timestamp bd = rs.getTimestamp("booking_date");
                    System.out.printf("%-8d %-8d %-8d %-20s %-20s %-6d %-20s%n",
                            bid, sid, seg, safeString(from), safeString(to), seats, bd == null ? "--" : bd.toString());
                }
                if (!any) {
                    System.out.println("No active bookings found to cancel.");
                    return;
                }
            }

            System.out.print("Enter Booking ID to cancel: ");
            int bookingId = Integer.parseInt(sc.nextLine().trim());

            // transaction: set booking status to cancelled and restore seats
            String getBooking = "SELECT segment_id, num_seats FROM bookings WHERE booking_id = ? AND user_id = ? AND status = 'booked' FOR UPDATE";
            String updBooking = "UPDATE bookings SET status = 'cancelled' WHERE booking_id = ?";
            String updSegment = "UPDATE schedule_segments SET seats_available = seats_available + ? WHERE segment_id = ?";

            try {
                con.setAutoCommit(false);

                int segId;
                int numSeats;
                try (PreparedStatement gb = con.prepareStatement(getBooking)) {
                    gb.setInt(1, bookingId);
                    gb.setString(2, userId);
                    try (ResultSet r = gb.executeQuery()) {
                        if (!r.next()) {
                            System.out.println("Booking not found or already cancelled.");
                            con.rollback();
                            return;
                        }
                        segId = r.getInt("segment_id");
                        numSeats = r.getInt("num_seats");
                    }
                }

                try (PreparedStatement ub = con.prepareStatement(updBooking)) {
                    ub.setInt(1, bookingId);
                    ub.executeUpdate();
                }

                try (PreparedStatement us = con.prepareStatement(updSegment)) {
                    us.setInt(1, numSeats);
                    us.setInt(2, segId);
                    us.executeUpdate();
                }

                con.commit();
                System.out.println("✅ Booking cancelled and seats restored.");

            } catch (SQLException transEx) {
                try { con.rollback(); } catch (SQLException rbe) { /* ignore */ }
                throw transEx;
            } finally {
                try { con.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
            }

        } catch (SQLException e) {
            System.out.println("SQL error during cancellation:");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    // -------------------------
    // 4) View booking history (booked + cancelled)
    // -------------------------
    private static void viewBookingHistory(String userId) {
        String sql =
            "SELECT b.booking_id, b.schedule_id, b.segment_id, st1.station_name AS from_name, " +
            "       st2.station_name AS to_name, ss.fare, b.num_seats, b.booking_date, b.status, t.name AS transport_name, s.date " +
            "FROM bookings b " +
            "JOIN schedule_segments ss ON b.segment_id = ss.segment_id " +
            "JOIN schedules s ON b.schedule_id = s.schedule_id " +
            "JOIN stations st1 ON ss.start_station_id = st1.station_id " +
            "JOIN stations st2 ON ss.end_station_id = st2.station_id " +
            "LEFT JOIN transports t ON s.transport_id = t.transport_id " +
            "WHERE b.user_id = ? " +
            "ORDER BY b.booking_date DESC";

        try (Connection con = Jdbc_Connection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                boolean any = false;
                System.out.println();
                System.out.printf("%-8s %-8s %-20s %-20s %-8s %-6s %-20s %-10s%n",
                        "BookID", "Sched", "From", "To", "Fare", "Seats", "BookingDate", "Status");
                System.out.println("-------------------------------------------------------------------------------------------");
                while (rs.next()) {
                    any = true;
                    int bid = rs.getInt("booking_id");
                    int sched = rs.getInt("schedule_id");
                    String from = rs.getString("from_name");
                    String to = rs.getString("to_name");
                    double fare = rs.getDouble("fare");
                    int seats = rs.getInt("num_seats");
                    Timestamp bd = rs.getTimestamp("booking_date");
                    String status = rs.getString("status");
                    System.out.printf("%-8d %-8d %-20s %-20s %-8.2f %-6d %-20s %-10s%n",
                            bid, sched, safeString(from), safeString(to), fare, seats,
                            bd == null ? "--" : bd.toString(), safeString(status));
                }
                if (!any) {
                    System.out.println("No bookings found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL error while fetching booking history:");
            e.printStackTrace();
        }
    }

    // -------------------------
    // Helper: return non-null string
    // -------------------------
    private static String safeString(String s) {
        return s == null ? "" : s;
    }
}

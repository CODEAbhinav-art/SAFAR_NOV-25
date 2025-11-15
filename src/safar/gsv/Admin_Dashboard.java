package safar.gsv;

import java.sql.Connection;
import java.util.Scanner;

public class Admin_Dashboard {
	

	
//	public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int choice;
//
//        System.out.println("========= 🧭 SAFAR ADMIN DASHBOARD =========");
//
//        while (true) {
//            System.out.println("\n========== MAIN MENU ==========");
//            System.out.println("1. Add Route with Stations");
//            System.out.println("2. View All Routes");
//            System.out.println("3. View Stations for a Route");
//            System.out.println("4. Add Schedule (with 60-day logic)");
//            System.out.println("5. Exit");
//            System.out.print("Enter your choice: ");
//            choice = sc.nextInt();
//            sc.nextLine(); // consume newline
//
//            switch (choice) {
//                case 1:
//                    Admin_Dahboard_Background.addRouteWithStations();
//                    break;
//
//                case 2:
//                    Admin_Dahboard_Background.displayRoutes();
//                    break;
//
//                case 3:
//                    Admin_Dahboard_Background.displayRoutes();
//                    System.out.print("Enter Route ID to view stations: ");
//                    int routeId = sc.nextInt();
//                    Admin_Dahboard_Background.displayStationsForRoute(routeId);
//                    break;
//
//                case 4:
//                    Admin_Dahboard_Background.addSchedule();
//                    break;
//
//                case 5:
//                    System.out.println("👋 Exiting Admin Dashboard. Goodbye!");
//                    sc.close();
//                    System.exit(0);
//                    break;
//
//                default:
//                    System.out.println("⚠️ Invalid choice. Please try again.");
//            }
//        }
//    }
	//////////////////////////////////////////////////////////////////////////////////////////////////////
			 public static void main(String[] args) {
			        Scanner sc = new Scanner(System.in);
			        while (true) {
			            System.out.println("\n======= SAFAR ADMIN MENU =======");
			            System.out.println("1. Add Transport");
//			            System.out.println("2. View Transports");
//			            System.out.println("3. Add Route for Transport");
//			            System.out.println("4. View Routes");
//			            System.out.println("5. Add Stations to Route");
//			            System.out.println("6. View Stations of a Route");
//			            System.out.println("7. Add Schedule (car -> single date ; others -> 60 days)");
//			            System.out.println("8. Refresh 60-day windows (auto-shift)");
			            System.out.println("2. Exit");
			            System.out.print("Choice: ");
		
			            int ch = Integer.parseInt(sc.nextLine().trim());
			            switch (ch) {
			            case 1:
			                Admin_Dashboard_Background_try.addTransport();
			                Admin_Dashboard_Background_try.displayTransports();
			                Admin_Dashboard_Background_try.addRouteForTransport();
			                Admin_Dashboard_Background_try.displayRoutes();
			                Admin_Dashboard_Background_try.addStationsForRoute();
			                
			                Admin_Dashboard_Background_try.displayRoutes();
			                System.out.print("Enter Route ID to display stations: ");
			                int rid = Integer.parseInt(sc.nextLine());
			                Admin_Dashboard_Background_try.displayStationsForRoute(rid);
			                
			                Admin_Dashboard_Background_try.addScheduleForTransport();
			                Admin_Dashboard_Background_try.refresh60DayWindow();
			                
			                break;
		
//			            case 2:
//			                Admin_Dashboard_Background_try.displayTransports();
//			                break;
//	
//			            case 3:
//			                Admin_Dashboard_Background_try.addRouteForTransport();
//			                break;
//		
//			            case 4:
//			                Admin_Dashboard_Background_try.displayRoutes();
//			                break;
//		
//			            case 5:
//			                Admin_Dashboard_Background_try.addStationsForRoute();
//			                break;
//		
//			            case 6:
//			                Admin_Dashboard_Background_try.displayRoutes();
//			                System.out.print("Enter Route ID to display stations: ");
//			                int rid = Integer.parseInt(sc.nextLine());
//			                Admin_Dashboard_Background_try.displayStationsForRoute(rid);
//			                break;
//		
//			            case 7:
//			                Admin_Dashboard_Background_try.addScheduleForTransport();
//			                break;
//		
//			            case 8:
//		                Admin_Dashboard_Background_try.refresh60DayWindow();
//			                break;
//		
			            case 2:
			                System.out.println("Exiting.");
		
			        }
			    }
		
		}
			
			
			


}

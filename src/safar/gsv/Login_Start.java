package safar.gsv;

import java.util.Scanner;

public class Login_Start {
	
	  public static void main(String[] args) {
	        Scanner sc = new Scanner(System.in);
	        while (true) {
	            System.out.println("\n===== SAFAR LOGIN SYSTEM =====");
	            System.out.println("1. Register");
	            System.out.println("2. Login");
	            System.out.println("3. Exit");
	            System.out.print("Choose option: ");
	            int choice = sc.nextInt();
	            sc.nextLine();

	            if (choice == 1) {
	                System.out.print("Enter User ID: ");
	                String id = sc.nextLine();
	                System.out.print("Enter Password: ");
	                String pass = sc.nextLine();
	                System.out.print("Enter Role (admin/user): ");
	                String role = sc.nextLine();

	                if (Login_Back_Operations.registerUser(id.toLowerCase(), pass.toLowerCase(), role.toLowerCase())) {
	                    System.out.println("Registration successful!");
	                } else {
	                    System.out.println("Registration failed. User may already exist.");
	                }

	            } else if (choice == 2) {
	                System.out.print("Enter User ID: ");
	                String id = sc.nextLine();
	                System.out.print("Enter Password: ");
	                String pass = sc.nextLine();

	                String role = Login_Back_Operations.loginUser(id, pass);
	                if (role != null) {
	                    System.out.println("Login successful! Role: " + role.toUpperCase());
	                    if (role.equals("admin")) {
	                        System.out.println("Redirecting to Admin Dashboard...");
	                    } else {
	                        System.out.println("Redirecting to User Dashboard...");
	                    }
	                } else {
	                    System.out.println("Invalid credentials!");
	                }

	            } else if (choice == 3) {
	                System.out.println("Exiting...");
	                break;
	            } else {
	                System.out.println("Invalid choice!");
	            }
	        }
	        sc.close();
	    }

}

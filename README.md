🧳 Safar Travel Management System

Safar is a modern travel booking and management platform built in Java (Swing + JDBC) with full SQL integration.
It provides a user-friendly travel booking system for trains and flights, along with a powerful Admin Dashboard for system monitoring and control.

🚀 Features Overview
👤 User Module

Secure Login, Signup, and Forgot Password screens

Wallet System (add money, auto fare deduction on booking)

Book Train Tickets (with real-time fare, class, and date filtering)

Book Flight Tickets (modern interface like IRCTC/Airline apps)

View booking history (Train & Flight separately)

Beautiful modern UI using Poppins font and color branding

🧑‍💼 Admin Module

Access via separate Admin Login

Dashboard overview with live stats:

Total Users 👥

Train Bookings 🚆

Flight Bookings ✈️

Total Revenue 💰

Manage Users

View all registered users

Delete users (auto-deletes related bookings)

View Train Bookings

List all train bookings by users

View Flight Bookings

List all flight bookings by users

🗂️ Project Structure
Safar/
│
├── src/
│   └── travel/
│       ├── DBConnection.java          # Database connection helper
│       ├── Login.java                 # User login page
│       ├── Signup.java                # User registration page
│       ├── ForgotPassword.java        # Password reset screen
│       ├── Dashboard.java             # User dashboard with wallet & navigation
│       ├── BookTrain.java             # Train booking module
│       ├── ViewTrainBookings.java     # View booked train tickets
│       ├── BookFlight.java            # Flight booking module
│       ├── ViewFlightBookings.java    # View booked flights
│       ├── AdminLogin.java            # Admin login screen
│       └── AdminDashboard.java        # Admin control panel (SQL-integrated)
│
├── assets/
│   ├── logo.png                       # App logo (used in login screens)
│   ├── icons/                         # Optional icons
│
└── README.md

💾 Database Schema (MySQL)

You’ll need a MySQL database named safar (or modify in DBConnection.java).

🧍‍♂️ Table: users
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE,
  fullname VARCHAR(100),
  email VARCHAR(100),
  password VARCHAR(100),
  wallet_balance DOUBLE DEFAULT 0
);

🚆 Table: train_bookings
CREATE TABLE train_bookings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50),
  train_name VARCHAR(100),
  journey_date DATE,
  class_type VARCHAR(50),
  passengers INT,
  fare DOUBLE,
  status VARCHAR(50)
);

✈️ Table: flight_bookings
CREATE TABLE flight_bookings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50),
  flight_code VARCHAR(50),
  class_type VARCHAR(50),
  passengers INT,
  fare DOUBLE,
  status VARCHAR(50)
);

🚉 Table: trains
CREATE TABLE trains (
  id INT AUTO_INCREMENT PRIMARY KEY,
  train_name VARCHAR(100),
  from_station VARCHAR(100),
  to_station VARCHAR(100),
  class_type VARCHAR(50),
  journey_date DATE,
  departure_time VARCHAR(10),
  arrival_time VARCHAR(10),
  duration VARCHAR(20),
  fare DOUBLE
);

🛫 Table: flights
CREATE TABLE flights (
  id INT AUTO_INCREMENT PRIMARY KEY,
  flight_code VARCHAR(50),
  from_city VARCHAR(100),
  to_city VARCHAR(100),
  class_type VARCHAR(50),
  departure_time VARCHAR(10),
  arrival_time VARCHAR(10),
  fare DOUBLE,
  airline VARCHAR(100)
);

⚙️ Setup Instructions

Clone this repository

git clone https://github.com/your-username/Safar-Travel-System.git
cd Safar-Travel-System


Set up Database

Create a database in MySQL named safar

Run the SQL scripts above

Configure DBConnection.java

private static final String URL = "jdbc:mysql://localhost:3306/safar";
private static final String USER = "root";
private static final String PASSWORD = "your_password";


Run the project

Compile all .java files under src/travel

Run Login.java for users or AdminLogin.java for admins

🖼️ Screenshots
Page	Description

	User login screen

	User dashboard with wallet

	Train booking page

	Flight booking page

	Admin control panel overview

(Add screenshots in /assets/screens/ folder for full preview)

🧠 Tech Stack
Component	Technology
Frontend (UI)	Java Swing
Backend	JDBC + MySQL
Language	Java 17+
Database	MySQL 8.0
IDE (Recommended)	IntelliJ IDEA / Eclipse
🔒 Security & Validation

Passwords encrypted before storage (optional upgrade)

SQL Injection-safe prepared statements

Input validation across all forms

Admin restricted access with login check

💡 Future Enhancements

 Add digital invoice generation (PDF)

 Add search filters in Admin panels

 User profile editing & password change

 Multi-city and round-trip bookings

 Analytics charts in admin dashboard

Aayush Praveen
Abhinav Mishra
Aditya Jaiswal
Aman Sharma

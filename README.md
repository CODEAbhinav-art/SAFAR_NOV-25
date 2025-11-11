🧳 Safar Travel Management System

Safar is a modern travel booking and management platform built in Java (Swing + JDBC) with full SQL integration.
It provides a user-friendly travel booking system for trains and flights, along with a powerful Admin Dashboard for system monitoring and control.

#NOTE:-
## 🗂️ 1️⃣ The “from–to–class” combinations that must exist

The code will only display a train when there’s at least one row in your table like this:

| train_name | from_station | to_station | class_type | departure_time | arrival_time | duration | fare |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Rajdhani Express | Delhi | Mumbai | 2A | 06:00 | 14:30 | 8h 30m | 1420 |
| Shatabdi Express | Delhi | Mumbai | CC | 09:15 | 16:00 | 6h 45m | 1250 |
| Garib Rath | Delhi | Mumbai | Sleeper | 11:00 | 19:45 | 8h 45m | 800 |
| Duronto Express | Mumbai | Chennai | 3A | 20:10 | 06:00 | 9h 50m | 1600 |
| Goa Express | Delhi | Goa | 3A | 05:45 | 17:30 | 11h 45m | 1350 |
| Konkan Express | Goa | Mumbai | Sleeper | 09:00 | 16:45 | 7h 45m | 900 |
| Rajdhani South | Bengaluru | Delhi | 1A | 06:30 | 20:00 | 13h 30m | 2850 |
| Jaipur Mail | Delhi | Jaipur | 2A | 07:00 | 12:00 | 5h 00m | 950 |
| Howrah Express | Kolkata | Delhi | 3A | 08:15 | 18:00 | 9h 45m | 1200 |
| Charminar Express | Hyderabad | Chennai | Sleeper | 09:00 | 18:00 | 9h 00m | 850 |
| Ahmedabad Mail | Mumbai | Ahmedabad | CC | 07:00 | 10:30 | 3h 30m | 700 |
| Pune Duronto | Mumbai | Pune | Sleeper | 05:30 | 08:15 | 2h 45m | 600 |

#For Flights:-

| flight_code | flight_name | from_city | to_city | class_type | airline | departure_time | arrival_time | fare |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| AI202 | Air India Express | Delhi | Mumbai | Economy | Air India | 06:30 | 08:45 | 6500 |
| 6E152 | IndiGo 152 | Delhi | Mumbai | Economy | IndiGo | 08:00 | 10:10 | 5500 |
| UK812 | Vistara 812 | Delhi | Goa | Economy | Vistara | 11:00 | 13:15 | 7200 |
| SG303 | SpiceJet 303 | Mumbai | Chennai | Economy | SpiceJet | 14:00 | 16:10 | 5800 |
| AI705 | Air India 705 | Kolkata | Delhi | Business | Air India | 07:15 | 09:30 | 11200 |
| 6E410 | IndiGo 410 | Bengaluru | Delhi | Economy | IndiGo | 05:50 | 08:20 | 6200 |
| UK205 | Vistara 205 | Mumbai | Goa | Premium Economy | Vistara | 13:00 | 14:15 | 7400 |
| AI301 | Air India 301 | Delhi | Chennai | Economy | Air India | 10:00 | 12:45 | 6700 |

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

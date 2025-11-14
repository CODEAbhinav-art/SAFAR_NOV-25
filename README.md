# 🧳 **Safar — Travel Management System (Java + MySQL)**

A modern, full-featured **Travel Booking Platform** built with **Java Swing + JDBC + MySQL**.
Safar allows users to book trains & flights, manage wallet balance, view booking history, and provides a full **Admin Dashboard** for managing the system.

## 🎥 **Demo**


https://github.com/user-attachments/assets/923f001a-a358-453e-9a6b-7c7abde3f8b5


# ⚠️ IMPORTANT

### ✔ Train & Flight search works only when required routes exist

Your database **must contain** the combinations listed below for correct functioning of booking modules.

---

# 🚆 **Required Train Route Entries**

| train_name        | from_station | to_station | class_type     | departure_time | arrival_time | duration | fare |
| ----------------- | ------------ | ---------- | -------------- | -------------- | ------------ | -------- | ---- |
| Rajdhani Express  | Delhi        | Mumbai     | 2A - AC 2 Tier | 06:00          | 14:30        | 8h 30m   | 1420 |
| Shatabdi Express  | Delhi        | Mumbai     | CC             | 09:15          | 16:00        | 6h 45m   | 1250 |
| Garib Rath        | Delhi        | Mumbai     | Sleeper        | 11:00          | 19:45        | 8h 45m   | 800  |
| Duronto Express   | Mumbai       | Chennai    | 3A - AC 3 Tier | 20:10          | 06:00        | 9h 50m   | 1600 |
| Goa Express       | Delhi        | Goa        | 3A             | 05:45          | 17:30        | 11h 45m  | 1350 |
| Konkan Express    | Goa          | Mumbai     | Sleeper        | 09:00          | 16:45        | 7h 45m   | 900  |
| Rajdhani South    | Bengaluru    | Delhi      | 1A             | 06:30          | 20:00        | 13h 30m  | 2850 |
| Jaipur Mail       | Delhi        | Jaipur     | 2A             | 07:00          | 12:00        | 5h 00m   | 950  |
| Howrah Express    | Kolkata      | Delhi      | 3A             | 08:15          | 18:00        | 9h 45m   | 1200 |
| Charminar Express | Hyderabad    | Chennai    | Sleeper        | 09:00          | 18:00        | 9h 00m   | 850  |
| Ahmedabad Mail    | Mumbai       | Ahmedabad  | CC             | 07:00          | 10:30        | 3h 30m   | 700  |
| Pune Duronto      | Mumbai       | Pune       | Sleeper        | 05:30          | 08:15        | 2h 45m   | 600  |

---

# ✈️ **Required Flight Route Entries**

| flight_code | airline   | from_city | to_city | class_type      | departure_time | arrival_time | fare  |
| ----------- | --------- | --------- | ------- | --------------- | -------------- | ------------ | ----- |
| AI202       | Air India | Delhi     | Mumbai  | Economy         | 06:30          | 08:45        | 6500  |
| 6E152       | IndiGo    | Delhi     | Mumbai  | Economy         | 08:00          | 10:10        | 5500  |
| UK812       | Vistara   | Delhi     | Goa     | Economy         | 11:00          | 13:15        | 7200  |
| SG303       | SpiceJet  | Mumbai    | Chennai | Economy         | 14:00          | 16:10        | 5800  |
| AI705       | Air India | Kolkata   | Delhi   | Business        | 07:15          | 09:30        | 11200 |
| 6E410       | IndiGo    | Bengaluru | Delhi   | Economy         | 05:50          | 08:20        | 6200  |
| UK205       | Vistara   | Mumbai    | Goa     | Premium Economy | 13:00          | 14:15        | 7400  |
| AI301       | Air India | Delhi     | Chennai | Economy         | 10:00          | 12:45        | 6700  |

---

# 🚀 **Features**

## 👤 User Features

* Login / Signup / Forgot Password
* Wallet Management (Top-Up + Auto Deduction)
* Book Train Tickets
* Book Flight Tickets
* View Train Bookings
* View Flight Bookings
* Clean UI with Poppins Font

## 🧑‍💼 Admin Features

* Admin Login
* Dashboard Overview
* View/Delete Users
* View All Train Bookings
* View All Flight Bookings
* Automatic cascade deletion for orphan records

---

# 📂 **Project Structure**

```
Safar/
│
├── src/travel/
│   ├── DBConnection.java
│   ├── Login.java
│   ├── Signup.java
│   ├── ForgotPassword.java
│   ├── Dashboard.java
│   ├── BookTrain.java
│   ├── ViewTrainBookings.java
│   ├── BookFlight.java
│   ├── ViewFlightBookings.java
│   ├── AdminLogin.java
│   └── AdminDashboard.java
│
├── assets/
│   ├── logo.png
│   └── icons/
│
└── README.md
```

---

# 💾 **Final Database Schema (MySQL)**

All tables **exactly as required by the project**.

## **1️⃣ users**

```sql
CREATE TABLE users (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255),
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(15),
    address VARCHAR(255),
    wallet_balance DOUBLE DEFAULT 0
);
```

## **2️⃣ trains**

```sql
CREATE TABLE trains (
    train_id INT AUTO_INCREMENT PRIMARY KEY,
    train_name VARCHAR(100),
    from_station VARCHAR(100),
    to_station VARCHAR(100),
    class_type VARCHAR(50),
    fare DOUBLE,
    departure_time VARCHAR(20),
    arrival_time VARCHAR(20),
    duration VARCHAR(30),
    journey_date DATE
);
```

## **3️⃣ flights**

```sql
CREATE TABLE flights (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_code VARCHAR(20),
    from_city VARCHAR(100),
    to_city VARCHAR(100),
    class_type VARCHAR(50),
    airline VARCHAR(50),
    fare DOUBLE,
    departure_time VARCHAR(20),
    arrival_time VARCHAR(20),
    duration VARCHAR(30),
    journey_date DATE
);
```

## **4️⃣ train_bookings**

```sql
CREATE TABLE train_bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    train_name VARCHAR(100),
    journey_date DATE,
    class_type VARCHAR(50),
    passengers INT,
    fare DOUBLE,
    status VARCHAR(20),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);
```

## **5️⃣ flight_bookings**

```sql
CREATE TABLE flight_bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    flight_code VARCHAR(20),
    class_type VARCHAR(50),
    passengers INT,
    fare DOUBLE,
    status VARCHAR(20),
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
);
```

## **6️⃣ routes**

```sql
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(100),
    destination VARCHAR(100),
    mode VARCHAR(50),
    cost INT,
    duration INT
);
```

### Sample Route Data

```sql
INSERT INTO routes (source, destination, mode, cost, duration) VALUES
('Delhi','Jaipur','Bus',500,5),
('Jaipur','Mumbai','Train',1500,12),
('Delhi','Mumbai','Flight',5000,2),
('Mumbai','Goa','Bus',700,9),
('Jaipur','Goa','Flight',3000,4),
('Delhi','Lucknow','Train',900,10),
('Lucknow','Varanasi','Bus',400,6),
('Varanasi','Kolkata','Flight',2500,2);
```

---

# ⚙️ Setup Instructions

## **1️⃣ Clone Repo**

```bash
git clone https://github.com/your-username/Safar-Travel-System.git
cd Safar-Travel-System
```

## **2️⃣ Create Database**

```sql
CREATE DATABASE safar;
USE safar;
```

## **3️⃣ Run All SQL Commands**

(Paste the schema above)

## **4️⃣ Configure `DBConnection.java`**

```java
private static final String URL = "jdbc:mysql://localhost:3306/safar";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

## **5️⃣ Run the Application**

* For users → run `Login.java`
* For admins → run `AdminLogin.java`

---

# 🧠 Tech Stack

| Component | Technology         |
| --------- | ------------------ |
| UI        | Java Swing         |
| Backend   | JDBC               |
| Database  | MySQL              |
| Language  | Java               |
| IDE       | IntelliJ / Eclipse |

---

# 🔒 Security

* Prepared Statements
* Encrypted Passwords (optional)
* Cascading deletes for consistency
* Admin-only access

---

# 🚀 Future Enhancements

* PDF ticket generation
* Booking cancellation
* Invoices and downloadable tickets
* Charts in Admin panel
* Round-trip & multi-city booking

---

# 👨‍💻 Contributors

* **Aayush Praveen**
* **Abhinav Mishra**
* **Aditya Jaiswal**
* **Aman Sharma**



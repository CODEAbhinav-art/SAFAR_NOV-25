# SAFAR_NOV-25
# ✈️ SAFAR by GSV — Travel Planner (Java Swing)

> A modern desktop-based **Travel Planner** application built using **Java Swing**, featuring an elegant login interface and modular navigation for user and admin access.

---

## 🖥️ Overview

This project is part of the **SAFAR by GSV** suite — a Java-based travel management platform designed for students, travelers, and administrators.  
It provides a clean and interactive **Login UI** using **Java Swing**, following modular principles and OOP-based window handling.

---

## 🚀 Features

### 🔐 **Login Interface**
- User-friendly login window with smooth navigation.  
- Form fields for **username** and **password** with input validation placeholders.  
- Buttons for:
  - **Login**
  - **Sign Up**
  - **Forgot Password**
  - **Admin Login**

### 🎨 **Modern UI Design**
- Minimal, professional color palette (deep blue and teal accents).
- Uses the **Poppins** font for consistent typography.
- Left panel features branding: `✈️ SAFAR by GSV`.
- Right panel includes the login form and interactive buttons.

### ⚙️ **Navigation**
- Navigates to:
  - `Signup.java` window for new user registration.
  - `Dashboard.java` for logged-in users.
  - `AdminLogin.java` for administrator access.
- Displays message dialogs for user feedback (using `JOptionPane`).

---

## 📸 UI Preview (Structure)

+-----------------------------------------------------------+

✈️ SAFAR by GSV (Blue Panel)
Username: [___________]
Password: [___________]
[Login] [Sign Up]
Forgot Password? Admin Login
+-----------------------------------------------------------+


## 🧩 Code Structure (As of Now)

src/
└── travel/
├── Login.java # Main Login Window (this file)
├── Signup.java # Registration form window
├── Dashboard.java # User dashboard post-login
└── AdminLogin.java # Admin authentication window


## 🧠 Technical Concepts Used

| Concept | Description |
|----------|-------------|
| **Swing Framework** | For GUI components like JFrame, JPanel, JButton, JLabel, JTextField, etc. |
| **Event Handling** | Implements `ActionListener` to handle button actions. |
| **Layout Management** | Combines BorderLayout and absolute positioning for clean UI alignment. |
| **OOP Design** | Uses encapsulation for UI components and modular navigation between frames. |
| **Custom Styling** | Uses custom fonts, color schemes, and minimalistic UI for modern aesthetics. |

---

## 🏗️ How to Run

### 1. **Clone the Repository**
```bash
git clone https://github.com/<your-username>/safar-travel-planner.git
cd safar-travel-planner
2. Compile the Source
Make sure you have JDK 8 or above installed.

bash
Copy code
javac -d bin src/travel/Login.java
3. Run the Application
bash
Copy code
java -cp bin travel.Login
🧰 Requirements
Tool	Version
Java (JDK)	8 or above
IDE (Optional)	IntelliJ IDEA / Eclipse / NetBeans
Font (Optional)	Poppins (for better UI rendering)

🧱 Future Enhancements
🔑 Database integration (MySQL or SQLite) for user authentication.

📡 API integration for travel booking.

📱 Responsive GUI with JavaFX or modern UI libraries.

🧭 Dashboard with trip planner, maps, and analytics.

🧑‍💼 Admin portal for user management and reports.

👩‍💻 Author
Developed by:
Aayush Praveen (24AI002)
Abhinav Mishra (24AI004)
Aditya Jaiswal (24AI007)
Aman Sharma (24AI010)

📝 License
This project is licensed under the MIT License — you’re free to modify and use it for personal or academic purposes.

❤️ Acknowledgements
Special thanks to the Java Swing Framework and the open-source community for enabling desktop-based UI development with elegant customization.

"Code the journey, travel the world — one frame at a time."

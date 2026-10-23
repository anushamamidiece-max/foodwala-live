# 🍔 Foodwala — Java Full Stack Food Delivery Web Application

<p align="center">

A full-stack food delivery web application built using Java, Servlets, JSP, JDBC and MySQL.

</p>

<p align="center">

🔎 Search Restaurants • 🍔 Browse Food • 🛒 Cart • 💳 Checkout • 📦 Orders

</p>

---

## 🌐 Project Links

### 🚀 Live Demo

🍔 **[Open Foodwala Live](https://foodwala-production.up.railway.app)**

### 🚀 Live Demo

🍔 **[Open Foodwala Live](https://foodwala-live.onrender.com)**

### 💻 GitHub Repository

📂 **[View Foodwala Source Code](https://github.com/anushamamidiece-max/Foodwala)**

### 🔐 Demo Login

- **Email:** `demo@foodwala.com`
- **Password:** `demo123`

> The live application is deployed using Docker and Apache Tomcat on Railway, with MySQL hosted on Railway.

---

# 📌 About The Project

Foodwala is a Zomato-style food delivery web application developed as a Java Full Stack project.

The application allows users to discover restaurants, browse menus, search for food, add items to a session-based cart, place orders and view their order history.

The project follows an MVC-style architecture using:

- Java Servlets
- JSP
- JDBC
- MySQL
- Apache Tomcat
- HTML
- CSS
- JavaScript

The application is designed with a fresh-food visual theme featuring rounded cards, hover effects, accent glow and click-ripple animations.

---

# 🎯 Project Objectives

The main objectives of Foodwala are:

- Build a complete Java Full Stack web application.
- Implement MVC architecture.
- Connect Java application with MySQL using JDBC.
- Implement CRUD database operations.
- Implement user registration and login.
- Implement session-based shopping cart functionality.
- Implement checkout and order placement.
- Store orders and order items in MySQL.
- Build a responsive and interactive food-delivery UI.
- Understand how frontend, backend and database layers communicate.

---

# 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| Frontend | HTML5, CSS3, JavaScript |
| Backend | Java Servlets |
| View | JSP |
| Database Connectivity | JDBC |
| Database | MySQL |
| Database Tool | MySQL Workbench |
| Server | Apache Tomcat 10.1 |
| Java Version | Java 17+ / Java 21 |
| Architecture | MVC |
| IDE | Eclipse IDE for Enterprise Java and Web Developers |
| JDBC Driver | MySQL Connector/J |

---

# 🏗️ Architecture

Foodwala follows an MVC-based layered architecture.

```text
                ┌─────────────────────┐
                │      Browser        │
                │ HTML/CSS/JavaScript │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │     JSP / View      │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │ Java Servlets       │
                │    Controller       │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │        DAO          │
                │    DAO Interface    │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │     DAOImpl         │
                │       JDBC          │
                └──────────┬──────────┘
                           │
                           ▼
                ┌─────────────────────┐
                │       MySQL         │
                └─────────────────────┘
                
               

**Tech stack (no frameworks, no external jars except the MySQL driver):**

| Layer        | Technology                                        |
|--------------|---------------------------------------------------|
| Server       | Apache Tomcat **10.1** (Jakarta Servlet 6)        |
| Backend      | Java Servlets + JSP + Scriptlets/EL               |
| Database     | MySQL (via plain **JDBC**, MySQL Workbench)       |
| Driver       | `mysql-connector-j-9.2.0.jar` (only dependency)   |
| Architecture | MVC — `controller` / `model` / `dao` / `daoimpl`  |
| Images       | Real food photos from **Unsplash & Pexels** (all links verified working) |

---

## ✨ Features (end-to-end, like Zomato)



**Demo login:** `demo@foodwala.com` / `demo123`

---

## 📁 Package structure

```
Foodwala
├── .project / .classpath / .settings        ← Eclipse project files (importable as-is)
├── sql/database.sql                          ← run this in MySQL Workbench first!
└── src/main
    ├── java/com/foodwala
    │   ├── controller/   ← Servlets (Home, Restaurant, Login, Register, Cart*,
    │   │                   Checkout, PlaceOrder, Orders, OrderSuccess) + AuthFilter
    │   ├── model/        ← User, Restaurant, MenuItem, CartItem, Order, OrderItem
    │   ├── dao/          ← UserDAO, RestaurantDAO, MenuItemDAO, OrderDAO (interfaces)
    │   ├── daoimpl/      ← JDBC implementations of all DAOs
    │   └── util/         ← DBUtil (JDBC connection), PasswordUtil (SHA-256)
    └── webapp
        ├── WEB-INF/web.xml
        ├── WEB-INF/lib/   ← put mysql-connector-j-9.2.0.jar HERE
        ├── css/style.css  ← full theme
        ├── js/app.js      ← ripple + toasts
        ├── home.jsp, menu.jsp, cart.jsp, checkout.jsp, login.jsp, register.jsp,
        │   orders.jsp, order-success.jsp, error.jsp, index.jsp
        └── includes/header.jsp, includes/footer.jsp
```

---

## 🚀 Setup (5 steps)

### 1. Create the database (MySQL Workbench)
1. Open **MySQL Workbench** and connect to your local server.
2. Open `sql/database.sql` (File → Open SQL Script).
3. Press **⚡ Execute All**. This creates the `foodwala` database, all 5 tables
   (`users`, `restaurants`, `menu_items`, `orders`, `order_items`) and seeds
   50 restaurants + 310 menu items + a demo user.

### 2. Add the MySQL connector
Copy **`mysql-connector-j-9.2.0.jar`** into:
```
src/main/webapp/WEB-INF/lib/
```
Then right-click the project → **Refresh** (the *Web App Libraries* container picks it up).

### 3. Set your DB password (if not root/root)
Edit `src/main/java/com/foodwala/util/DBUtil.java`:
```java
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "root";   // ← your MySQL password
```

### 4. Import into Eclipse
1. **File → Import → General → Existing Projects into Workspace**
2. Select the `Foodwala` folder → Finish.
3. Make sure a **Tomcat 10.1 runtime** exists:
   *Window → Preferences → Server → Runtime Environments → Add → Apache Tomcat v10.1*.
   (If the project shows a build-path warning, right-click project → *Build Path →
   Configure Build Path → Libraries* and point the broken "Apache Tomcat v10.1" entry
   to your runtime.)
4. JDK must be **Java 17+** (Tomcat 10.1 requirement).

### 5. Run
Right-click project → **Run As → Run on Server** → choose Tomcat 10.1 → Finish.

Open: **http://localhost:8080/Foodwala/**

---

## 🔗 URL map

| URL                      | What it does                              |
|--------------------------|-------------------------------------------|
| `/Foodwala/`             | Home — all 50 restaurants                 |
| `/restaurants?q=biryani` | Search                                    |
| `/restaurants?cuisine=Pizza` | Cuisine filter                        |
| `/restaurant?id=1`       | Restaurant menu with ADD buttons          |
| `/cart`                  | Cart + bill details                       |
| `/checkout`              | Address + payment (login required)        |
| `/orders`                | Order history (login required)            |
| `/login`, `/register`, `/logout` | Auth                              |

## 🛠 Troubleshooting

- **"MySQL Connector/J not found"** → jar missing from `WEB-INF/lib`.
- **Blank page / DB error box** → MySQL not running, `database.sql` not executed,
  or wrong credentials in `DBUtil.java`.
- **404 on `/Foodwala/`** → project not deployed; check Servers view in Eclipse.

Images are loaded from Unsplash/Pexels, so an internet connection is needed while browsing.
                

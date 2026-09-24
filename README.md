# Intelligent Java-Based Framework for Smart Courier Booking, Assignment, and Shipment Tracking

This is a complete Courier Management System built using Java, Spring Boot, Spring Security, Hibernate, MySQL, and Thymeleaf. It provides three main portals:
- **Customer:** Register, book couriers, view history, and track shipments.
- **Admin:** Manage customers, staff, bookings, and assign delivery staff.
- **Delivery Staff:** View assigned shipments and update their real-time status.

## Prerequisites
- **Java 17+**
- **Maven**
- **XAMPP** (for local MySQL database)

## Local Setup Instructions (XAMPP)

1. **Start XAMPP**
   - Open the XAMPP Control Panel.
   - Start the **MySQL** module (click "Start" next to MySQL).
   - *(Optional)* Start **Apache** if you want to use phpMyAdmin to view the database.

2. **Create the Database**
   - The application expects a database named `courier_db`.
   - By default, Spring Boot will attempt to create it if it doesn't exist, provided the XAMPP MySQL root user has privileges. 
   - Alternatively, open your browser and go to `http://localhost/phpmyadmin/`.
   - Click on "Databases", enter `courier_db`, and click "Create".

3. **Configure the Application**
   - The database connection is configured in `src/main/resources/application.properties`.
   - It is set to connect to `localhost:3306/courier_db` with username `root` and an empty password (default for XAMPP).

4. **Start the Application**
   - Open a terminal or command prompt in the root of the project.
   - Run the following command:
     ```bash
     mvn spring-boot:run
     ```
   - Alternatively, you can use the provided wrapper:
     ```bash
     .\mvnw spring-boot:run
     ```

5. **Access the Application**
   - Open your web browser and navigate to: `http://localhost:8080`

## Initial Login Details

When the application starts, it automatically creates a default Admin account:
- **Email:** `admin@admin.com`
- **Password:** `admin`

You can use this account to log in and create Delivery Staff accounts, or you can register a new Customer account from the homepage.

## Deployment Consideration

For deployment to environments like **InfinityFree** (which supports PHP/MySQL but not natively Java Spring Boot), keep the database configuration separate. You can build the Spring Boot `.jar` file and deploy it to a Java-compatible host (e.g., Heroku, AWS Elastic Beanstalk, Render), while pointing the `spring.datasource.url` to the remote MySQL database provided by InfinityFree.

package com.smartcourier.config;

import com.smartcourier.model.*;
import com.smartcourier.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private TrackingHistoryRepository trackingRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() > 1) {
            return; // Database already seeded or has existing data
        }
        
        // Ensure Admin exists
        if (userRepository.findByEmail("admin@admin.com") == null) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }

        // Create Delivery Staff
        User staff1 = createStaff("Ravi Kumar", "ravi@staff.com", "9876543210");
        User staff2 = createStaff("Suresh Menon", "suresh@staff.com", "9876543211");
        User staff3 = createStaff("Karthik Raj", "karthik@staff.com", "9876543212");

        // Create Customers
        User c1 = createCustomer("Arun Sharma", "arun@mail.com", "9000000001", "12, Anna Nagar, Chennai");
        User c2 = createCustomer("Priya Patel", "priya@mail.com", "9000000002", "45, RS Puram, Coimbatore");
        User c3 = createCustomer("Vikram Singh", "vikram@mail.com", "9000000003", "8, Cantonment, Trichy");
        User c4 = createCustomer("Anjali Desai", "anjali@mail.com", "9000000004", "22, Thillai Nagar, Erode");
        User c5 = createCustomer("Rahul Verma", "rahul@mail.com", "9000000005", "56, Katpadi, Vellore");

        // Create Bookings
        Booking b1 = createBooking(c1, "Arun Sharma", "Chennai", "Manoj", "Madurai", ShipmentStatus.DELIVERED, LocalDateTime.now().minusDays(5));
        createHistory(b1, ShipmentStatus.DELIVERED, "Madurai Hub", "Delivered safely");

        Booking b2 = createBooking(c2, "Priya Patel", "Coimbatore", "Divya", "Salem", ShipmentStatus.OUT_FOR_DELIVERY, LocalDateTime.now().minusDays(1));
        assignStaff(b2, staff1);
        createHistory(b2, ShipmentStatus.OUT_FOR_DELIVERY, "Salem Local", "Out for delivery");

        Booking b3 = createBooking(c3, "Vikram", "Trichy", "Ramesh", "Chennai", ShipmentStatus.IN_TRANSIT, LocalDateTime.now().minusDays(2));
        assignStaff(b3, staff2);
        createHistory(b3, ShipmentStatus.IN_TRANSIT, "Villupuram Hub", "In transit to destination");

        Booking b4 = createBooking(c4, "Anjali", "Erode", "Sanjay", "Tiruppur", ShipmentStatus.ASSIGNED, LocalDateTime.now().minusHours(10));
        assignStaff(b4, staff3);
        
        Booking b5 = createBooking(c5, "Rahul", "Vellore", "Kiran", "Kanchipuram", ShipmentStatus.BOOKED, LocalDateTime.now().minusHours(2));
        Booking b6 = createBooking(c1, "Arun", "Chennai", "Vendor", "Bangalore", ShipmentStatus.DELIVERED, LocalDateTime.now().minusDays(10));
        createHistory(b6, ShipmentStatus.DELIVERED, "Bangalore South", "Received by security");

        Booking b7 = createBooking(c2, "Priya", "Coimbatore", "Client", "Kochi", ShipmentStatus.IN_TRANSIT, LocalDateTime.now().minusDays(1));
        assignStaff(b7, staff1);
        createHistory(b7, ShipmentStatus.IN_TRANSIT, "Palakkad", "Crossed border");
        
        Booking b8 = createBooking(c3, "Vikram", "Trichy", "Office", "Madurai", ShipmentStatus.BOOKED, LocalDateTime.now().minusMinutes(30));
        Booking b9 = createBooking(c4, "Anjali", "Erode", "Home", "Chennai", ShipmentStatus.DELIVERED, LocalDateTime.now().minusDays(4));
        createHistory(b9, ShipmentStatus.DELIVERED, "Chennai Central", "Handed to recipient");

        Booking b10 = createBooking(c5, "Rahul", "Vellore", "Store", "Hosur", ShipmentStatus.ASSIGNED, LocalDateTime.now().minusHours(5));
        assignStaff(b10, staff2);
    }

    private User createStaff(String name, String email, String phone) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode("password123"));
        u.setPhone(phone);
        u.setRole(Role.DELIVERY_STAFF);
        return userRepository.save(u);
    }

    private User createCustomer(String name, String email, String phone, String address) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode("password123"));
        u.setPhone(phone);
        u.setAddress(address);
        u.setRole(Role.CUSTOMER);
        return userRepository.save(u);
    }

    private Booking createBooking(User customer, String sName, String pickup, String rName, String delivery, ShipmentStatus status, LocalDateTime date) {
        Booking b = new Booking();
        b.setTrackingId("TRK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        b.setCustomer(customer);
        b.setSenderName(sName);
        b.setSenderPhone(customer.getPhone());
        b.setPickupAddress(pickup);
        b.setReceiverName(rName);
        b.setReceiverPhone("9800000000");
        b.setDeliveryAddress(delivery);
        b.setPackageDetails("Electronics / Documents");
        b.setPackageWeight(2.5);
        b.setDeliveryType("Standard");
        b.setDeliveryCharge(120.0);
        b.setStatus(status);
        b.setBookingDate(date);
        Booking saved = bookingRepository.save(b);
        
        // Initial history
        TrackingHistory hist = new TrackingHistory();
        hist.setBooking(saved);
        hist.setStatus(ShipmentStatus.BOOKED);
        hist.setLocation(pickup);
        hist.setRemarks("Booking confirmed");
        hist.setUpdateTime(date);
        trackingRepository.save(hist);
        
        return saved;
    }

    private void assignStaff(Booking b, User staff) {
        Assignment a = new Assignment();
        a.setBooking(b);
        a.setDeliveryStaff(staff);
        a.setAssignmentDate(b.getBookingDate().plusHours(1));
        assignmentRepository.save(a);
        
        TrackingHistory hist = new TrackingHistory();
        hist.setBooking(b);
        hist.setStatus(ShipmentStatus.ASSIGNED);
        hist.setLocation(b.getPickupAddress());
        hist.setRemarks("Agent assigned for pickup");
        hist.setUpdateTime(b.getBookingDate().plusHours(1));
        trackingRepository.save(hist);
    }

    private void createHistory(Booking b, ShipmentStatus status, String location, String remarks) {
        TrackingHistory hist = new TrackingHistory();
        hist.setBooking(b);
        hist.setStatus(status);
        hist.setLocation(location);
        hist.setRemarks(remarks);
        hist.setUpdateTime(LocalDateTime.now());
        trackingRepository.save(hist);
    }
}

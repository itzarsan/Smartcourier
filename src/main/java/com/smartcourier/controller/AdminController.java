package com.smartcourier.controller;

import com.smartcourier.model.Booking;
import com.smartcourier.model.Role;
import com.smartcourier.model.User;
import com.smartcourier.service.AssignmentService;
import com.smartcourier.service.BookingService;
import com.smartcourier.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private AssignmentService assignmentService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        java.util.List<com.smartcourier.model.Booking> allBookings = bookingService.getAllBookings();
        model.addAttribute("totalBookings", allBookings.size());
        model.addAttribute("totalRevenue", allBookings.stream().mapToDouble(b -> b.getDeliveryCharge() != null ? b.getDeliveryCharge() : 0.0).sum());
        model.addAttribute("pendingBookings", allBookings.stream().filter(b -> b.getStatus() == com.smartcourier.model.ShipmentStatus.BOOKED).count());
        model.addAttribute("activeBookings", allBookings.stream().filter(b -> b.getStatus() != com.smartcourier.model.ShipmentStatus.BOOKED && b.getStatus() != com.smartcourier.model.ShipmentStatus.DELIVERED && b.getStatus() != com.smartcourier.model.ShipmentStatus.FAILED).count());
        model.addAttribute("deliveredBookings", allBookings.stream().filter(b -> b.getStatus() == com.smartcourier.model.ShipmentStatus.DELIVERED).count());
        
        model.addAttribute("totalCustomers", userService.getUsersByRole(com.smartcourier.model.Role.CUSTOMER).size());
        model.addAttribute("totalStaff", userService.getUsersByRole(com.smartcourier.model.Role.DELIVERY_STAFF).size());
        return "admin/dashboard";
    }
    
    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("customers", userService.getUsersByRole(Role.CUSTOMER));
        return "admin/customers";
    }
    
    @GetMapping("/staff")
    public String staff(Model model) {
        model.addAttribute("staffs", userService.getUsersByRole(Role.DELIVERY_STAFF));
        return "admin/staff";
    }
    
    @GetMapping("/bookings")
    public String bookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("staffs", userService.getUsersByRole(Role.DELIVERY_STAFF));
        return "admin/bookings";
    }
    
    @PostMapping("/assign")
    public String assignStaff(@RequestParam Long bookingId, @RequestParam Long staffId) {
        Booking booking = bookingService.findById(bookingId);
        User staff = userService.findById(staffId);
        if (booking != null && staff != null) {
            assignmentService.assignStaff(booking, staff);
        }
        return "redirect:/admin/bookings?assigned";
    }
}

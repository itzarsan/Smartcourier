package com.smartcourier.controller;

import com.smartcourier.model.Booking;
import com.smartcourier.model.User;
import com.smartcourier.security.CustomUserDetails;
import com.smartcourier.service.BookingService;
import com.smartcourier.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private TrackingService trackingService;
    
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        com.smartcourier.model.User user = userDetails.getUser();
        java.util.List<com.smartcourier.model.Booking> bookings = bookingService.getCustomerBookings(user);
        model.addAttribute("user", user);
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("deliveredBookings", bookings.stream().filter(b -> b.getStatus() == com.smartcourier.model.ShipmentStatus.DELIVERED).count());
        model.addAttribute("activeBookings", bookings.stream().filter(b -> b.getStatus() != com.smartcourier.model.ShipmentStatus.BOOKED && b.getStatus() != com.smartcourier.model.ShipmentStatus.DELIVERED && b.getStatus() != com.smartcourier.model.ShipmentStatus.FAILED).count());
        model.addAttribute("pendingBookings", bookings.stream().filter(b -> b.getStatus() == com.smartcourier.model.ShipmentStatus.BOOKED).count());
        model.addAttribute("recentBookings", bookings.stream().limit(5).toList());
        return "customer/dashboard";
    }
    
    @GetMapping("/book")
    public String bookForm(Model model) {
        model.addAttribute("booking", new Booking());
        return "customer/book";
    }
    
    @PostMapping("/book")
    public String createBooking(@AuthenticationPrincipal CustomUserDetails userDetails, 
                                Booking booking, Model model) {
        Booking saved = bookingService.createBooking(booking, userDetails.getUser());
        return "redirect:/customer/history?booked=" + saved.getTrackingId();
    }
    
    @GetMapping("/history")
    public String history(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("bookings", bookingService.getCustomerBookings(userDetails.getUser()));
        return "customer/history";
    }
}

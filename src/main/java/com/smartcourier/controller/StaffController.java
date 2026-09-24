package com.smartcourier.controller;

import com.smartcourier.model.Assignment;
import com.smartcourier.model.Booking;
import com.smartcourier.model.ShipmentStatus;
import com.smartcourier.security.CustomUserDetails;
import com.smartcourier.service.AssignmentService;
import com.smartcourier.service.BookingService;
import com.smartcourier.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private TrackingService trackingService;
    
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        List<Assignment> assignments = assignmentService.getAssignmentsByStaff(userDetails.getUser());
        model.addAttribute("totalAssigned", assignments.size());
        model.addAttribute("activeDeliveries", assignments.stream().filter(a -> a.getBooking().getStatus() != com.smartcourier.model.ShipmentStatus.DELIVERED && a.getBooking().getStatus() != com.smartcourier.model.ShipmentStatus.FAILED).count());
        model.addAttribute("completedDeliveries", assignments.stream().filter(a -> a.getBooking().getStatus() == com.smartcourier.model.ShipmentStatus.DELIVERED).count());
        model.addAttribute("assignments", assignments);
        return "staff/dashboard";
    }
    
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long bookingId, 
                               @RequestParam ShipmentStatus status,
                               @RequestParam String location,
                               @RequestParam String remarks) {
        Booking booking = bookingService.findById(bookingId);
        if (booking != null) {
            bookingService.updateStatus(booking, status);
            trackingService.addHistory(booking, status, remarks, location);
        }
        return "redirect:/staff/dashboard?updated";
    }
}

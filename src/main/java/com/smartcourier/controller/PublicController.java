package com.smartcourier.controller;

import com.smartcourier.model.Booking;
import com.smartcourier.service.BookingService;
import com.smartcourier.service.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {

    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private TrackingService trackingService;

    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/track")
    public String track(@RequestParam(required = false) String trackingId, Model model) {
        if (trackingId != null && !trackingId.isEmpty()) {
            Booking booking = bookingService.findByTrackingId(trackingId);
            if (booking != null) {
                model.addAttribute("booking", booking);
                model.addAttribute("history", trackingService.getHistory(booking));
            } else {
                model.addAttribute("error", "Tracking ID not found.");
            }
        }
        return "track";
    }
}

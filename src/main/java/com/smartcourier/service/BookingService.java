package com.smartcourier.service;

import com.smartcourier.model.Booking;
import com.smartcourier.model.ShipmentStatus;
import com.smartcourier.model.User;
import com.smartcourier.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private TrackingService trackingService;
    
    public double calculateCharge(Double weight, String type) {
        double baseCharge = 50.0;
        double weightCharge = weight * 10.0; // 10 per kg
        double typeMultiplier = "Express".equalsIgnoreCase(type) ? 1.5 : 1.0;
        return (baseCharge + weightCharge) * typeMultiplier;
    }
    
    public Booking createBooking(Booking booking, User customer) {
        booking.setCustomer(customer);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(ShipmentStatus.BOOKED);
        
        // Generate unique tracking ID
        String trackingId = "TRK" + System.currentTimeMillis();
        booking.setTrackingId(trackingId);
        
        // Calculate charge
        booking.setDeliveryCharge(calculateCharge(booking.getPackageWeight(), booking.getDeliveryType()));
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Add history
        trackingService.addHistory(savedBooking, ShipmentStatus.BOOKED, "Booking confirmed.", "Origin");
        
        return savedBooking;
    }
    
    public List<Booking> getCustomerBookings(User customer) {
        return bookingRepository.findByCustomerOrderByBookingDateDesc(customer);
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }
    
    public Booking findByTrackingId(String trackingId) {
        return bookingRepository.findByTrackingId(trackingId).orElse(null);
    }
    
    public void updateStatus(Booking booking, ShipmentStatus status) {
        booking.setStatus(status);
        bookingRepository.save(booking);
    }
}

package com.smartcourier.service;

import com.smartcourier.model.Booking;
import com.smartcourier.model.ShipmentStatus;
import com.smartcourier.model.TrackingHistory;
import com.smartcourier.repository.TrackingHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrackingService {
    
    @Autowired
    private TrackingHistoryRepository trackingRepository;
    
    public void addHistory(Booking booking, ShipmentStatus status, String remarks, String location) {
        TrackingHistory history = new TrackingHistory();
        history.setBooking(booking);
        history.setStatus(status);
        history.setRemarks(remarks);
        history.setLocation(location);
        history.setUpdateTime(LocalDateTime.now());
        trackingRepository.save(history);
    }
    
    public List<TrackingHistory> getHistory(Booking booking) {
        return trackingRepository.findByBookingOrderByUpdateTimeDesc(booking);
    }
}

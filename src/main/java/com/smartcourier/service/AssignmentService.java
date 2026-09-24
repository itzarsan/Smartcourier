package com.smartcourier.service;

import com.smartcourier.model.Assignment;
import com.smartcourier.model.Booking;
import com.smartcourier.model.ShipmentStatus;
import com.smartcourier.model.User;
import com.smartcourier.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {
    
    @Autowired
    private AssignmentRepository assignmentRepository;
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private TrackingService trackingService;
    
    public void assignStaff(Booking booking, User staff) {
        Assignment assignment = assignmentRepository.findByBooking(booking).orElse(new Assignment());
        assignment.setBooking(booking);
        assignment.setDeliveryStaff(staff);
        assignment.setAssignmentDate(LocalDateTime.now());
        assignment.setStatus("Active");
        assignmentRepository.save(assignment);
        
        bookingService.updateStatus(booking, ShipmentStatus.ASSIGNED);
        trackingService.addHistory(booking, ShipmentStatus.ASSIGNED, "Assigned to delivery staff: " + staff.getName(), "Hub");
    }
    
    public List<Assignment> getAssignmentsByStaff(User staff) {
        return assignmentRepository.findByDeliveryStaffOrderByAssignmentDateDesc(staff);
    }
    
    public Assignment getAssignmentByBooking(Booking booking) {
        return assignmentRepository.findByBooking(booking).orElse(null);
    }
}

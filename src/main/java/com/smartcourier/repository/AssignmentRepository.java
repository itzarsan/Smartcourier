package com.smartcourier.repository;

import com.smartcourier.model.Assignment;
import com.smartcourier.model.Booking;
import com.smartcourier.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByDeliveryStaffOrderByAssignmentDateDesc(User staff);
    Optional<Assignment> findByBooking(Booking booking);
}

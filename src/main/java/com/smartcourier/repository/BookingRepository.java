package com.smartcourier.repository;

import com.smartcourier.model.Booking;
import com.smartcourier.model.User;
import com.smartcourier.model.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByTrackingId(String trackingId);
    List<Booking> findByCustomerOrderByBookingDateDesc(User customer);
    List<Booking> findByStatus(ShipmentStatus status);
}

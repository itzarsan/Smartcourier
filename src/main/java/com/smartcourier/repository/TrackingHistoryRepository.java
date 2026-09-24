package com.smartcourier.repository;

import com.smartcourier.model.TrackingHistory;
import com.smartcourier.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrackingHistoryRepository extends JpaRepository<TrackingHistory, Long> {
    List<TrackingHistory> findByBookingOrderByUpdateTimeDesc(Booking booking);
}

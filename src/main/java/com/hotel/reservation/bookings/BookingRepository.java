package com.hotel.reservation.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Modifying
    @Transactional
    void deleteByIdAndRoomId(@Param("id") Integer id, @Param("roomId") Integer roomId);
}

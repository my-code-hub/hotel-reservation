package com.hotel.reservation.bookings;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class BookingFront {
    private Integer id;
    private Integer version;
    private LocalDate checkin;
    private LocalDate checkout;
    private Integer customerId;
}

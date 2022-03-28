package com.hotel.reservation.bookings;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ValidBookingRequest
public class BookingRequest {
    private LocalDate checkin;
    private LocalDate checkout;
    @NotNull private Integer customerId;
}

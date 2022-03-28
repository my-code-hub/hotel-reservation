package com.hotel.reservation.bookings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CreateBookingRequestValidator
        implements ConstraintValidator<ValidBookingRequest, BookingRequest> {

    @Override
    public boolean isValid(BookingRequest request, ConstraintValidatorContext context) {
        LocalDate checkin = request.getCheckin();
        LocalDate checkout = request.getCheckout();
        return checkin != null && checkout != null
                && (checkout.isAfter(checkin) || checkout.isEqual(checkin));
    }
}

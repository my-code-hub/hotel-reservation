package com.hotel.reservation.bookings;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreateBookingRequestValidator.class)
public @interface ValidBookingRequest {
    String message () default "Invalid booking period";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}

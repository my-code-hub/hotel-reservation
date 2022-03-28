package com.hotel.reservation.bookings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CreateBookingRequestValidatorTest {

    private CreateBookingRequestValidator validator = new CreateBookingRequestValidator();

    @Mock ConstraintValidatorContext context;

    private LocalDate TODAY = LocalDate.now();

    @Test
    void isValid_success() {
        BookingRequest request = request(TODAY, TODAY.plusDays(1));

        assertThat(validator.isValid(request, context)).isTrue();
    }

    @Test
    void isValid_checkinAndCheckoutOnSameDay() {
        BookingRequest request = request(TODAY, TODAY);

        assertThat(validator.isValid(request, context)).isTrue();
    }

    @Test
    void isValid_checkinIsNull() {
        BookingRequest request = request(null, TODAY);

        assertThat(validator.isValid(request, context)).isFalse();
    }

    @Test
    void isValid_checkoutIsNull() {
        BookingRequest request = request(TODAY, null);

        assertThat(validator.isValid(request, context)).isFalse();
    }

    @Test
    void isValid_checkoutIsBeforeCheckin() {
        BookingRequest request = request(TODAY, TODAY.minusDays(2));

        assertThat(validator.isValid(request, context)).isFalse();
    }

    private BookingRequest request(LocalDate checkin, LocalDate checkout) {
        BookingRequest request = new BookingRequest();
        request.setCheckin(checkin);
        request.setCheckout(checkout);
        return request;
    }
}

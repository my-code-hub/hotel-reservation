package com.hotel.reservation.bookings;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/rooms")
@RequiredArgsConstructor
class RoomBookingsController {

    private final BookingsService bookingsService;

    @GetMapping("/{roomId}/bookings")
    public List<BookingFront> bookings(@PathVariable Integer roomId) {
        return bookingsService.bookings(roomId);
    }

    @PostMapping("/{roomId}/bookings")
    public BookingFront create(@PathVariable Integer roomId,
                               @Valid @RequestBody BookingRequest booking) {
        return bookingsService.create(roomId, booking);
    }

    @DeleteMapping("/{roomId}/bookings/{bookingId}")
    public void delete(@PathVariable Integer roomId,
                       @PathVariable Integer bookingId) {
        bookingsService.delete(roomId, bookingId);
    }
}

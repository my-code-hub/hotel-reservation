package com.hotel.reservation.bookings;

import com.hotel.customers.Customer;
import com.hotel.customers.CustomerService;
import com.hotel.errors.NotFoundException;
import com.hotel.reservation.rooms.Room;
import com.hotel.reservation.rooms.RoomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class BookingsService {

    private final BookingRepository bookingRepository;
    private final RoomsService roomsService;
    private final BookingMapper bookingMapper;
    private final CustomerService customerService;

    public List<BookingFront> bookings(Integer roomId) {
        return ofNullable(roomsService.room(roomId).getBookings())
                .orElse(emptyList())
                .stream()
                .map(bookingMapper::mapFrom)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingFront create(Integer roomId, BookingRequest request) {
        Room room = roomsService.room(roomId, request.getCheckin(), request.getCheckout())
                .orElseThrow(() -> new NotFoundException("Room " + roomId + " is not available."));

        Integer customerId = request.getCustomerId();
        Customer customer = customerService.customer(customerId)
                .orElseThrow(() -> new NotFoundException("Customer " + customerId + " not found."));

        Booking booking = bookingMapper.mapFrom(request);
        booking.setRoom(room);
        booking.setCustomer(customer);

        bookingRepository.save(booking);
        return bookingMapper.mapFrom(booking);
    }

    public void delete(Integer roomId, Integer bookingId) {
        bookingRepository.deleteByIdAndRoomId(bookingId, roomId);
    }
}

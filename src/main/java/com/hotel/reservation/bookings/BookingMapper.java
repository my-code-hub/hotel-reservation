package com.hotel.reservation.bookings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    Booking mapFrom(BookingRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    BookingFront mapFrom(Booking booking);
}

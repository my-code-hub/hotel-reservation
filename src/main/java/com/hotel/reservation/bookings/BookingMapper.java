package com.hotel.reservation.bookings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {

    Booking mapFrom(BookingRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    BookingFront mapFrom(Booking booking);
}

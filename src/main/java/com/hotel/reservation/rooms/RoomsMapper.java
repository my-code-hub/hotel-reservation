package com.hotel.reservation.rooms;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomsMapper {

    RoomFront mapFrom(Room room);

    Room mapFrom(CreateRoomRequest request);
}

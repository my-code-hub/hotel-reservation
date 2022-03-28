package com.hotel.reservation.rooms;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomsMapper {

    RoomFront mapFrom(Room room);

    Room mapFrom(CreateRoomRequest request);
}

package com.hotel.reservation.rooms;

import lombok.Data;

@Data
public class UpdateRoomRequest extends CreateRoomRequest {
    private Integer version;
}

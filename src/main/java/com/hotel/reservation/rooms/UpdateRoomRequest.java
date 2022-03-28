package com.hotel.reservation.rooms;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateRoomRequest extends CreateRoomRequest {
    private Integer version;
}

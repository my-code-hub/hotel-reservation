package com.hotel.reservation.rooms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomsAvailability {
    private Integer busy;
    private Integer available;
}

package com.hotel.reservation.rooms;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
public class CreateRoomRequest {
    @NotNull
    @Min(1)
    private Integer number;
}

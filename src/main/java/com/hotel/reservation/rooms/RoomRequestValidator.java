package com.hotel.reservation.rooms;

import com.hotel.errors.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RoomRequestValidator {

    private final RoomsRepository roomsRepository;

    void validate(CreateRoomRequest request) {
        validate(null, request);
    }

    void validate(Integer existingRoomNumber, CreateRoomRequest request) {
        if (!isValid(existingRoomNumber, request)) {
            throw new ValidationException("Unavailable room number");
        }
    }

    private boolean isValid(Integer existingRoomNumber, CreateRoomRequest request) {
        Integer roomNumber = request.getNumber();
        if (request instanceof UpdateRoomRequest && roomNumber.equals(existingRoomNumber)) {
            return true;
        }
        return roomsRepository.findByNumber(roomNumber).isEmpty();
    }
}

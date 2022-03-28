package com.hotel.reservation.rooms;

import com.hotel.errors.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomRequestValidatorTest {

    @InjectMocks
    private RoomRequestValidator validator;

    @Mock private RoomsRepository roomsRepository;

    @Test
    void validate_validCreateRequest() {
        Integer roomNumber = 1;
        CreateRoomRequest request = new CreateRoomRequest();
        request.setNumber(roomNumber);

        when(roomsRepository.findByNumber(roomNumber)).thenReturn(Optional.empty());

        validator.validate(request);

        verify(roomsRepository).findByNumber(roomNumber);
    }

    @Test
    void validate_invalidCreateRequest() {
        Integer roomNumber = 1;
        CreateRoomRequest request = new CreateRoomRequest();
        request.setNumber(roomNumber);

        when(roomsRepository.findByNumber(roomNumber)).thenReturn(Optional.of(new Room()));

        assertThatThrownBy(() -> validator.validate(request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Unavailable room number");

        verify(roomsRepository).findByNumber(roomNumber);
    }

    @Test
    void validateUpdateRequest_validRoomNumber() {
        Integer roomNumber = 1;
        UpdateRoomRequest request = new UpdateRoomRequest();
        request.setNumber(roomNumber);

        validator.validate(roomNumber, request);

        verifyNoInteractions(roomsRepository);
    }

    @Test
    void validateUpdateRequest_unavailableRoomNumber() {
        Integer roomNumber = 1;
        Integer existingRoomNumber = 2;
        UpdateRoomRequest request = new UpdateRoomRequest();
        request.setNumber(roomNumber);

        when(roomsRepository.findByNumber(roomNumber)).thenReturn(Optional.of(new Room()));

        assertThatThrownBy(() -> validator.validate(existingRoomNumber, request))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Unavailable room number");

        verify(roomsRepository).findByNumber(roomNumber);
    }
}
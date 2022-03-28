package com.hotel.reservation.rooms;

import com.hotel.errors.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class RoomsService {

    private final RoomsRepository repository;
    private final RoomsMapper roomsMapper;
    private final RoomRequestValidator roomRequestValidator;

    public RoomFront roomFront(Integer roomId) {
        return roomsMapper.mapFrom(room(roomId));
    }

    public Room room(Integer roomId) {
        return repository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room " + roomId + " not found"));
    }

    public Optional<Room> room(Integer roomId, LocalDate availableFrom, LocalDate availableTo) {
        return repository.availableRoom(roomId, availableFrom, availableTo);
    }

    public List<RoomFront> rooms() {
        return repository.findAll()
                .stream()
                .map(roomsMapper::mapFrom)
                .collect(Collectors.toList());
    }

    public RoomFront create(CreateRoomRequest request) {
        roomRequestValidator.validate(request);
        Room room = roomsMapper.mapFrom(request);
        return roomsMapper.mapFrom(repository.save(room));
    }

    public RoomFront update(Integer roomId, UpdateRoomRequest request) {
        Room existing = room(roomId);

        roomRequestValidator.validate(existing.getNumber(), request);

        existing.setNumber(request.getNumber());
        existing.setVersion(request.getVersion());
        repository.save(existing);
        return roomsMapper.mapFrom(existing);
    }

    public void delete(Integer roomId) {
        repository.deleteById(roomId);
    }

    public RoomsAvailability availability(LocalDate from, LocalDate to) {
        RoomsStatistics statistics = repository.availabilityStatistics(from, to);
        Integer availableRoomCount = ofNullable(statistics.getAvailableRoomCount()).orElse(0);
        Integer busyRoomCount = statistics.getTotalRoomCount() - availableRoomCount;
        return new RoomsAvailability(busyRoomCount, availableRoomCount);
    }

    public List<RoomFront> availableRooms(LocalDate from, LocalDate to) {
        return repository.availableRooms(from, to)
                .stream()
                .map(roomsMapper::mapFrom)
                .collect(Collectors.toList());
    }
}

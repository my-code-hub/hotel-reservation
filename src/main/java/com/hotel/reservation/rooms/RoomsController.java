package com.hotel.reservation.rooms;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rooms")
public class RoomsController {

    private final RoomsService roomsService;

    @GetMapping("/{id}")
    public RoomFront room(@PathVariable Integer id) {
        return roomsService.roomFront(id);
    }

    @GetMapping
    public List<RoomFront> rooms() {
        return roomsService.rooms();
    }

    @PostMapping
    public RoomFront create(@Valid @RequestBody CreateRoomRequest room) {
        return roomsService.create(room);
    }

    @PutMapping("/{id}")
    public RoomFront update(@PathVariable Integer id, @Valid @RequestBody UpdateRoomRequest room) {
        return roomsService.update(id, room);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        roomsService.delete(id);
    }

    @GetMapping("/availability-statistics")
    public RoomsAvailability availabilityStatistics(@RequestParam LocalDate from,
                                                    @RequestParam LocalDate to) {
        return roomsService.availability(from, to);
    }

    @GetMapping("/available")
    public List<RoomFront> availableRooms(@RequestParam LocalDate from,
                                          @RequestParam LocalDate to) {
        return roomsService.availableRooms(from, to);
    }
}

package com.hotel.reservation.rooms;

import com.hotel.customers.Customer;
import com.hotel.customers.CustomerRepository;
import com.hotel.reservation.bookings.Booking;
import com.hotel.reservation.bookings.BookingRepository;
import com.hotel.test.BaseIT;
import io.restassured.http.ContentType;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDate;

import static com.hotel.test.FileReader.readFile;
import static com.hotel.test.JSONAssertMatcher.jsonMatcher;
import static com.hotel.test.JSONAssertMatcher.lenientJsonMatcher;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class RoomsControllerIT extends BaseIT {

    @Autowired private RoomsRepository repository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        bookingRepository.deleteAll();
        customerRepository.deleteAll();
        repository.deleteAll();

        customer = insertCustomer();
    }

    @Test
    public void getRoom() throws IOException {
        Room room1 = insertRoom(123);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("/v1/rooms/{id}", room1.getId())
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .body(jsonMatcher("rooms/get_one/response.json", room1.getId()));
    }

    @Test
    public void getRoom_notFound() throws IOException {
        int roomNumber = 142345;
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("/v1/rooms/{id}", roomNumber)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(jsonMatcher("validation/room_not_found_response.json", roomNumber));
    }

    @Test
    public void getRooms() throws IOException {
        Room room1 = insertRoom(1);
        Room room2 = insertRoom(2);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("/v1/rooms")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .body(jsonMatcher("rooms/get_all/response.json", room1.getId(), room2.getId()));
    }

    @Test
    public void createRoom() throws IOException {
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("rooms/create/request.json"))
        .when()
            .post("/v1/rooms")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .body(lenientJsonMatcher("rooms/create/response.json"));
    }

    @Test
    public void createRoom_roomNumberIsNull() throws IOException {
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body("{}")
        .when()
            .post("/v1/rooms")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(jsonMatcher("validation/validation_error.json"));
    }

    @Test
    public void createRoom_duplicateRoomNumber() throws IOException {
        insertRoom(1);
        
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("rooms/create/request.json"))
       .when()
            .post("/v1/rooms")
            .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(jsonMatcher("rooms/unavailable_room.json"));
    }

    @Test
    public void updateRoom() throws IOException {
        Room room = insertRoom();
        Integer roomId = room.getId();

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("rooms/update/request.json", 2))
        .when()
            .put("/v1/rooms/{id}", roomId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
        .body(jsonMatcher("rooms/update/response.json", roomId));
    }

    @Test
    public void updateRoom_notFound() throws IOException {
        int roomNumber = 1241324235;
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("rooms/update/request.json", roomNumber))
        .when()
            .put("/v1/rooms/{id}", roomNumber)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(jsonMatcher("validation/room_not_found_response.json", roomNumber));
    }

    @Test
    public void updateRoom_duplicateRoomNumber() throws IOException {
        Room room = insertRoom();
        Integer roomId = room.getId();

        Integer duplicateRoomNumber = 2;
        insertRoom(duplicateRoomNumber);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("rooms/update/request.json", duplicateRoomNumber))
       .when()
            .put("/v1/rooms/{id}", roomId)
       .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(jsonMatcher("rooms/unavailable_room.json"));
    }

    @Test
    public void deleteRoom() {
        Room room = insertRoom();

        assertThat(repository.findAll()).hasSize(1);

        given()
           .accept(ContentType.JSON)
           .contentType(ContentType.JSON)
       .when()
           .delete("/v1/rooms/{id}", room.getId())
       .then()
           .statusCode(HttpStatus.OK.value());

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    public void getAvailabilityStatistics() throws IOException {
        AvailableRoomsTestData data = setupTestData();

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .queryParam("from", data.getAvailableFrom().toString())
            .queryParam("to", data.getAvailableTo().toString())
            .get("/v1/rooms/availability-statistics")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .body(jsonMatcher("rooms/availability_statistics/response.json"));
    }

    @Test
    public void getAvailabilityStatistics_rangeNotSpecified() throws IOException {
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
       .when()
            .get("/v1/rooms/availability-statistics")
       .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(jsonMatcher("validation/validation_error.json"));
    }

    @Test
    public void getAvailableRooms() throws IOException {
        AvailableRoomsTestData data = setupTestData();

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .queryParam("from", data.getAvailableFrom().toString())
            .queryParam("to", data.getAvailableTo().toString())
            .get("/v1/rooms/available")
        .then()
           .contentType(ContentType.JSON)
           .statusCode(HttpStatus.OK.value())
           .body(jsonMatcher("rooms/available/response.json", data.getAvailableRoomIds()));
    }

    @Test
    public void getAvailableRooms_rangeNotSpecified() throws IOException {
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("/v1/rooms/available")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(jsonMatcher("validation/validation_error.json"));
    }

    /*
        The user's selected period:
        --------[---------]-------
        Booking no1
        [-----]-------------------
        Booking no2
        --------------------[----]
        Booking no3
        -----[----]---------------
        Booking no4
        -----------[---]----------
        Booking no5
        --------------[--------]--
        Booking no6
        -----[----------------]---
     */
    private AvailableRoomsTestData setupTestData() {
        LocalDate now = LocalDate.now();

        //available rooms
        Room room1 = insertRoom();
        Room room2 = insertRoomWithBooking(1, now.minusDays(9), now.minusDays(7));
        Room room3 = insertRoomWithBooking(2, now.plusDays(7), now.plusDays(9));

        //unavailable rooms
        insertRoomWithBooking(3, now.minusDays(7), now.minusDays(2));
        insertRoomWithBooking(4, now.minusDays(2), now.plusDays(2));
        insertRoomWithBooking(5, now.plusDays(2), now.plusDays(7));
        insertRoomWithBooking(6, now.minusDays(7), now.plusDays(7));

        return AvailableRoomsTestData.builder()
                .availableFrom(now.minusDays(5))
                .availableTo(now.plusDays(5))
                .availableRoomIds(new Integer[] {room1.getId(), room2.getId(), room3.getId()})
                .build();
    }

    @Getter
    @Builder
    static class AvailableRoomsTestData {
        private LocalDate availableFrom;
        private LocalDate availableTo;
        private Object[] availableRoomIds;
    }

    private Room insertRoom() {
        return insertRoom(1337);
    }

    private Room insertRoom(int number) {
        Room room = new Room();
        room.setNumber(number);
        return repository.save(room);
    }

    private Room insertRoomWithBooking(int roomNumber, LocalDate checkin, LocalDate checkout) {
        Room room = new Room();
        room.setNumber(roomNumber);
        repository.save(room);

        Booking booking = new Booking();
        booking.setCheckin(checkin);
        booking.setCheckout(checkout);
        booking.setRoom(room);
        booking.setCustomer(customer);
        bookingRepository.save(booking);
        return room;
    }

    private Customer insertCustomer() {
        Customer customer = new Customer();
        customer.setUsername("john.doe@mail.com");
        return customerRepository.save(customer);
    }
}

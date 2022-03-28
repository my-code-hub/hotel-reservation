package com.hotel.reservation.bookings;

import com.hotel.customers.Customer;
import com.hotel.customers.CustomerRepository;
import com.hotel.reservation.rooms.Room;
import com.hotel.reservation.rooms.RoomsRepository;
import com.hotel.test.BaseIT;
import com.hotel.test.JSONAssertMatcher;
import io.restassured.http.ContentType;
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

public class RoomBookingsControllerIT extends BaseIT {

    @Autowired private RoomsRepository repository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        bookingRepository.deleteAll();
        repository.deleteAll();
        customerRepository.deleteAll();

        customer = insertCustomer();
    }

    @Test
    public void getBookings() throws IOException {
        LocalDate now = LocalDate.now();
        Booking booking = insertRoomWithBooking(5, now, now);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
       .when()
            .get("/v1/rooms/{id}/bookings", booking.getRoom().getId())
       .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .body(jsonMatcher("/bookings/get_all/response.json", booking.getId(), now.toString(),
                    now.toString(), customer.getId()));
    }

    @Test
    public void getBookings_roomNotFound() throws IOException {
        int roomNumber = 1242354;

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get("/v1/rooms/{id}/bookings", roomNumber)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(jsonMatcher("/validation/room_not_found_response.json", roomNumber));
    }

    @Test
    public void createBooking() throws IOException {
        Room room = insertRoom(1337);

        String now = LocalDate.now().toString();

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("bookings/create/request.json", now, now, customer.getId()))
       .when()
            .post("/v1/rooms/{roomId}/bookings", room.getId())
       .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.OK.value())
            .body(lenientJsonMatcher("bookings/create/response.json", now, now, customer.getId()));
    }

    @Test
    public void createBooking_roomNotFound() throws IOException {
        String now = LocalDate.now().toString();
        int roomNumber = 112356;

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("bookings/create/request.json", now, now, customer.getId()))
            .when()
            .post("/v1/rooms/{roomId}/bookings", roomNumber)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(jsonMatcher("/validation/room_not_available_response.json", roomNumber));
    }

    @Test
    public void createBooking_invalidBookingPeriod() throws IOException {
        Room room = insertRoom(1337);

        LocalDate now = LocalDate.now();
        String checkout = now.minusDays(5).toString();
        
        given()
             .accept(ContentType.JSON)
             .contentType(ContentType.JSON)
             .body(readFile("bookings/create/request.json", now.toString(), checkout, customer.getId()))
        .when()
            .post("/v1/rooms/{roomId}/bookings", room.getId())
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(JSONAssertMatcher.jsonMatcher("bookings/create/incorrect_checkin_range_response.json"));
    }

    @Test
    public void createBooking_roomAlreadyBooked() throws IOException {
        LocalDate now = LocalDate.now();
        Booking booking = insertRoomWithBooking(5, now, now);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .body(readFile("bookings/create/request.json", now.toString(), now.toString(), customer.getId()))
        .when()
            .post("/v1/rooms/{roomId}/bookings", booking.getRoom().getId())
        .then()
            .contentType(ContentType.JSON)
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body(jsonMatcher("bookings/create/validation_error_response.json", booking.getRoom().getId()));
    }

    @Test
    public void deleteBooking() {
        LocalDate now = LocalDate.now();
        Booking booking = insertRoomWithBooking(5, now, now);

        assertThat(bookingRepository.findAll()).hasSize(1);

        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .delete("/v1/rooms/{roomId}/bookings/{bookingId}", booking.getRoom().getId(), booking.getId())
        .then()
            .statusCode(HttpStatus.OK.value());

        assertThat(bookingRepository.findAll()).isEmpty();
    }

    private Booking insertRoomWithBooking(int roomNumber, LocalDate checkin, LocalDate checkout) {
        Room room = new Room();
        room.setNumber(roomNumber);
        repository.saveAndFlush(room);

        Booking booking = new Booking();
        booking.setCheckin(checkin);
        booking.setCheckout(checkout);
        booking.setRoom(room);
        booking.setCustomer(customer);
        bookingRepository.saveAndFlush(booking);
        return booking;
    }

    private Room insertRoom(int roomNumber) {
        Room room = new Room();
        room.setNumber(roomNumber);
        return repository.saveAndFlush(room);
    }

    private Customer insertCustomer() {
        Customer customer = new Customer();
        customer.setUsername("john.doe@mail.com");
        return customerRepository.save(customer);
    }
}

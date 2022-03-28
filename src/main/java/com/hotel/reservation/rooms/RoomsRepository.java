package com.hotel.reservation.rooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomsRepository extends JpaRepository<Room, Integer> {

    String SELECT_UNAVAILABLE_ROOM_IDS = "(" +
                                         "  SELECT room_id " +
                                         "  FROM booking " +
                                         "  WHERE (checkin <= :from AND checkout >= :from) " +
                                         "    OR (checkin < :to AND checkout >= :to) " +
                                         "    OR (:from <= checkin AND :to >= checkin) " +
                                         ")";

    @Query(value = "SELECT " +
                   "(SELECT count(*) FROM room) as totalRoomCount, " +
                   "(SELECT count(*) " +
                   "        FROM room " +
                   "        WHERE id NOT IN " + SELECT_UNAVAILABLE_ROOM_IDS +
                   ") as availableRoomCount ", nativeQuery = true)
    RoomsStatistics availabilityStatistics(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = "SELECT * " +
                   "FROM room " +
                   "WHERE id NOT IN " + SELECT_UNAVAILABLE_ROOM_IDS, nativeQuery = true)
    List<Room> availableRooms(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = "SELECT * " +
                   "FROM room " +
                   "WHERE :roomId = id AND id NOT IN " + SELECT_UNAVAILABLE_ROOM_IDS, nativeQuery = true)
    Optional<Room> availableRoom(@Param("roomId") Integer roomId, @Param("from") LocalDate from,
                                 @Param("to") LocalDate to);

    Optional<Room> findByNumber(@Param("number") Integer roomNumber);
}

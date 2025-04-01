package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.exception.RoomNotFoundException;
import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.domain.repository.RoomRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    @Counted(value = "rooms.created", description = "Total habitaciones creadas")
    public UUID createRoom(String number) {
        UUID roomId = UUID.randomUUID();

        Room room = new Room(
                roomId,
                number
        );

        Log.info("Creating new room with ID: " + roomId);

        roomRepository.save(room);

        Log.info("Room created successfully with ID: " + room.getId());

        return roomId;
    }

    @Transactional
    @Timed(value = "room.fetch.all.time", description = "Tiempo en obtener todas las habitaciones")
    public List<Room> getAllRooms() {
        Log.info("Fetching all rooms");

        return roomRepository.findAll();
    }

    @Transactional
    public Room getRoomById(UUID roomId) {
        return roomRepository.findById(roomId)
                .map(room -> {
                    Log.info("Room found: " + room.getId());
                    return room;
                })
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + roomId));
    }

    @Transactional
    public void deleteRoom(UUID roomId) {
        Log.info("Deleting room with ID: " + roomId);

        roomRepository.deleteById(roomId);

        Log.info("Room deleted successfully with ID: " + roomId);
    }
}
package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.domain.repository.RoomRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public UUID createRoom(String number) {
        UUID roomId = UUID.randomUUID();

        Room room = new Room(
                roomId,
                number
        );

        roomRepository.save(room);

        return roomId;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    public void deleteRoom(UUID roomId) {
        roomRepository.deleteById(roomId);
    }
}
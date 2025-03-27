package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.domain.repository.RoomRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Counted(name = "rooms_created", description = "Total habitaciones creadas")
    public UUID createRoom(String number) {
        UUID roomId = UUID.randomUUID();

        Room room = new Room(
                roomId,
                number
        );

        roomRepository.save(room);

        return roomId;
    }

    @Timed(name = "room_fetch_all_time", description = "Tiempo en obtener todas las habitaciones", unit = MetricUnits.MILLISECONDS)
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
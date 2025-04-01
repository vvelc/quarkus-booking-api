package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.exception.RoomNotFoundException;
import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.domain.repository.RoomRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class RoomServiceTest {

    @InjectMock
    RoomRepository roomRepository;

    @Inject
    RoomService roomService;

    @Test
    void shouldCreateRoomAndReturnId() {
        String roomNumber = "101";

        UUID resultId = roomService.createRoom(roomNumber);

        // Verify that the Room was saved with the correct number and generated ID
        ArgumentCaptor<Room> roomCaptor = ArgumentCaptor.forClass(Room.class);
        Mockito.verify(roomRepository).save(roomCaptor.capture());
        Room savedRoom = roomCaptor.getValue();
        assertEquals(roomNumber, savedRoom.getNumber());
        // The returned UUID should match the saved Room's ID
        assertEquals(savedRoom.getId(), resultId);
        assertNotNull(resultId);
    }

    @Test
    void shouldReturnAllRoomsList() {
        Room dummyRoom = new Room(UUID.randomUUID(), "202");
        List<Room> dummyList = List.of(dummyRoom);
        Mockito.when(roomRepository.findAll()).thenReturn(dummyList);

        List<Room> result = roomService.getAllRooms();

        Mockito.verify(roomRepository).findAll();
        assertEquals(dummyList, result);
        assertFalse(result.isEmpty());
        assertEquals(dummyRoom, result.getFirst());
    }

    @Test
    void shouldReturnRoomWhenFoundById() {
        UUID roomId = UUID.randomUUID();
        Room room = new Room(roomId, "303");
        Mockito.when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        Room result = roomService.getRoomById(roomId);

        Mockito.verify(roomRepository).findById(roomId);
        assertSame(room, result);
    }

    @Test
    void shouldThrowRoomNotFoundExceptionWhenNotExists() {
        UUID missingId = UUID.randomUUID();
        Mockito.when(roomRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(missingId));
        Mockito.verify(roomRepository).findById(missingId);
    }

    @Test
    void shouldDeleteRoomSuccessfully() {
        UUID roomId = UUID.randomUUID();

        roomService.deleteRoom(roomId);

        // Verify that the repository delete method was called for the given room ID
        Mockito.verify(roomRepository).deleteById(roomId);
    }
}

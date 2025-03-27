package com.vvelc.booking.interface_.rest.mapper;

import com.vvelc.booking.domain.model.Room;
import com.vvelc.booking.interface_.rest.dto.RoomResponse;

public class RoomDtoMapper {
    public static RoomResponse toDto(Room room) {
        return new RoomResponse(
                room.getId()
        );
    }
}

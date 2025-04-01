package com.vvelc.booking.domain.exception;

public class RoomNotFoundException extends NotFoundException {
    public RoomNotFoundException(String message) {
        super(message);
    }
}

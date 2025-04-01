package com.vvelc.booking.domain.exception;

public class OverlappingBookingException extends BadRequestException {
    public OverlappingBookingException(String message) {
        super(message);
    }
}

package com.vvelc.booking.domain.exception;

public class InvalidBookingDatesException extends BadRequestException {
    public InvalidBookingDatesException(String message) {
        super(message);
    }
}

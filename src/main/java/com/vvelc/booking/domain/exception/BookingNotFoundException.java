package com.vvelc.booking.domain.exception;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}

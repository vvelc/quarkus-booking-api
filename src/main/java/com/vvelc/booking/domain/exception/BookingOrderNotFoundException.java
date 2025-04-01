package com.vvelc.booking.domain.exception;

public class BookingOrderNotFoundException extends NotFoundException {
    public BookingOrderNotFoundException(String message) {
        super(message);
    }
}

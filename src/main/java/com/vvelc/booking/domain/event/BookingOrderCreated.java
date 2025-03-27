package com.vvelc.booking.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingOrderCreated {
    private UUID bookingOrderId;
    private UUID roomId;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
package com.vvelc.booking.domain.event;

import com.vvelc.booking.domain.common.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingOrderStatusEvent {
    private UUID bookingOrderId;
    private BookingStatus status;
    private String reason;
}
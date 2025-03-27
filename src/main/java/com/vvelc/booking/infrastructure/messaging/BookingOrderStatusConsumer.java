package com.vvelc.booking.infrastructure.messaging;

import com.vvelc.booking.application.service.BookingOrderService;
import com.vvelc.booking.application.service.BookingService;
import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.model.BookingOrder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BookingOrderStatusConsumer {

    @Inject
    BookingService bookingService;

    @Inject
    BookingOrderService bookingOrderService;

    @Incoming("booking-order-status-in")
    public void onBookingOrderStatus(BookingOrderStatusEvent event) {
        switch (event.getStatus()) {
            case CONFIRMED -> {
                BookingOrder order = bookingOrderService.getBookingOrderById(event.getBookingOrderId());

                bookingOrderService.updateBookingOrderStatus(event.getBookingOrderId(), BookingStatus.CONFIRMED);

                bookingService.createFromConfirmedOrder(event, order);
            }

            case REJECTED -> {
                bookingOrderService.updateBookingOrderStatus(event.getBookingOrderId(), BookingStatus.REJECTED);
                System.out.println("Booking order rejected: " + event.getBookingOrderId()
                        + " - Reason: " + event.getReason());
                // TODO: Change to logger
            }

            default -> throw new IllegalArgumentException("Unexpected status: " + event.getStatus());
        }
    }
}
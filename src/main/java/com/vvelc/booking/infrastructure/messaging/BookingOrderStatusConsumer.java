package com.vvelc.booking.infrastructure.messaging;

import com.vvelc.booking.application.service.BookingOrderService;
import com.vvelc.booking.application.service.BookingService;
import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.model.BookingOrder;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BookingOrderStatusConsumer {

    @Inject
    BookingService bookingService;

    @Inject
    BookingOrderService bookingOrderService;

    @Transactional
    @Incoming("booking-order-status-in")
    public void onBookingOrderStatus(BookingOrderStatusEvent event) {
        Log.info("Received booking order status event for ID: " + event.getBookingOrderId());

        switch (event.getStatus()) {
            case CONFIRMED -> {
                Log.info("Status is CONFIRMED. Creating booking...");

                BookingOrder order = bookingOrderService.getBookingOrderById(event.getBookingOrderId());

                bookingOrderService.updateBookingOrderStatus(event.getBookingOrderId(), BookingStatus.CONFIRMED);

                bookingService.createFromConfirmedOrder(event, order);
            }

            case REJECTED -> {
                Log.info("Status is REJECTED. Booking order rejected for ID: "
                        + event.getBookingOrderId() + " - Reason: " + event.getReason());

                bookingOrderService.updateBookingOrderStatus(event.getBookingOrderId(), BookingStatus.REJECTED);
            }

            default -> {
                Log.warn("Unknown status: " + event.getStatus());
                throw new IllegalArgumentException("Unexpected status: " + event.getStatus()
                        + " for booking order ID: " + event.getBookingOrderId());
            }
        }
    }
}
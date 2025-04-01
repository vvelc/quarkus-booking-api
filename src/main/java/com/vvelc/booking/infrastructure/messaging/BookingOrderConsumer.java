package com.vvelc.booking.infrastructure.messaging;

import com.vvelc.booking.application.service.AvailabilityService;
import com.vvelc.booking.domain.event.BookingOrderCreatedEvent;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BookingOrderConsumer {

    @Inject
    AvailabilityService availabilityService;

    @Incoming("booking-order-created-in")
    public void onBookingOrderCreated(BookingOrderCreatedEvent event) {
        Log.info("Received booking order created event: " + event.getBookingOrderId());
        Log.info("Dispatching availability check for booking order: " + event.getBookingOrderId());
        availabilityService.checkAvailabilityAndRespond(event);
    }
}

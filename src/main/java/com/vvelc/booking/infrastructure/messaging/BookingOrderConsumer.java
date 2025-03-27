package com.vvelc.booking.infrastructure.messaging;

import com.vvelc.booking.application.service.AvailabilityService;
import com.vvelc.booking.domain.event.BookingOrderCreated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BookingOrderConsumer {

    @Inject
    AvailabilityService availabilityService;

    @Incoming("booking-order-created-in")
    public void onBookingOrderCreated(BookingOrderCreated event) {
        availabilityService.checkAvailabilityAndRespond(event);
    }
}

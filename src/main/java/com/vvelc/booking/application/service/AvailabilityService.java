package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.event.BookingOrderCreatedEvent;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import com.vvelc.booking.domain.repository.BookingRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AvailabilityService {

    @Inject
    @Channel("booking-order-status-out")
    Emitter<BookingOrderStatusEvent> availabilityEmitter;

    private final BookingRepository bookingRepository;
    private final BookingOrderRepository bookingOrderRepository;

    @Timed(value = "availability.check.time", description = "Tiempo en procesar disponibilidad")
    @Transactional
    public void checkAvailabilityAndRespond(BookingOrderCreatedEvent event) {
        Log.info("Checking availability for booking order: " + event.getBookingOrderId());
        boolean conflict = bookingRepository.hasOverlappingBooking(
                event.getRoomId(),
                event.getCheckIn(),
                event.getCheckOut()
        );

        if (conflict) {
            Log.info("Room is NOT available, rejecting booking order: " + event.getBookingOrderId());
            handleConflict(event);
        } else {
            Log.info("Room is available, confirming booking order: " + event.getBookingOrderId());
            handleAvailability(event);
        }
    }

    @Counted(value = "availability.conflicts", description = "Cantidad de rechazos por conflicto de disponibilidad")
    public void handleConflict(BookingOrderCreatedEvent event) {
        updateOrderStatus(event.getBookingOrderId(), BookingStatus.REJECTED);
        notifyRejection(event.getBookingOrderId());
    }

    @Counted(value = "availability.confirmed", description = "Cantidad de bookings confirmados por disponibilidad")
    public void handleAvailability(BookingOrderCreatedEvent event) {
        updateOrderStatus(event.getBookingOrderId(), BookingStatus.CONFIRMED);
        notifyConfirmation(event.getBookingOrderId());
    }

    private void updateOrderStatus(UUID orderId, BookingStatus status) {
        Log.info("Updating status to " + status + " for order ID: " + orderId);
        bookingOrderRepository.updateStatus(orderId, status.name());
    }

    private void notifyRejection(UUID orderId) {
        Log.info("Sending rejection event for booking order: " + orderId);
        availabilityEmitter.send(new BookingOrderStatusEvent(
                orderId,
                BookingStatus.REJECTED,
                "Room not available")
        );
    }

    private void notifyConfirmation(UUID orderId) {
        Log.info("Sending confirmation event for booking order: " + orderId);
        availabilityEmitter.send(new BookingOrderStatusEvent(
                orderId,
                BookingStatus.CONFIRMED,
                "Room available")
        );
    }
}
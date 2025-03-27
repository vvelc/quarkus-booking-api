package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.event.BookingOrderCreated;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import com.vvelc.booking.domain.repository.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AvailabilityService {

    @Inject
    @Channel("booking-order-status-out")
    Emitter<Object> availabilityEmitter;

    private final BookingRepository bookingRepository;
    private final BookingOrderRepository bookingOrderRepository;

    public void checkAvailabilityAndRespond(BookingOrderCreated event) {
        boolean conflict = bookingRepository.hasOverlappingBooking(
                event.getRoomId(),
                event.getCheckIn(),
                event.getCheckOut()
        );

        if (conflict) {
            handleConflict(event);
        } else {
            handleAvailability(event);
        }
    }

    private void handleConflict(BookingOrderCreated event) {
        updateOrderStatus(event.getBookingOrderId(), BookingStatus.REJECTED);
        notifyRejection(event.getBookingOrderId());
    }

    private void handleAvailability(BookingOrderCreated event) {
        updateOrderStatus(event.getBookingOrderId(), BookingStatus.CONFIRMED);
        notifyConfirmation(event.getBookingOrderId());
    }

    private void updateOrderStatus(UUID orderId, BookingStatus status) {
        bookingOrderRepository.updateStatus(orderId, status.name());
    }

    private void notifyRejection(UUID orderId) {
        availabilityEmitter.send(new BookingOrderStatusEvent(
                orderId,
                BookingStatus.REJECTED,
                "Room not available")
        );
    }

    private void notifyConfirmation(UUID orderId) {
        availabilityEmitter.send(new BookingOrderStatusEvent(
                orderId,
                BookingStatus.CONFIRMED,
                "Room available")
        );
    }
}
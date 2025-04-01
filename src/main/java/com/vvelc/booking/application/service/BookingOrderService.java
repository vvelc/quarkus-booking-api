package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderCreatedEvent;
import com.vvelc.booking.domain.exception.BookingOrderNotFoundException;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class BookingOrderService {
    @Inject
    @Channel("booking-order-created-out")
    Emitter<BookingOrderCreatedEvent> bookingOrderEmitter;

    private final BookingOrderRepository bookingOrderRepository;

    @Transactional
    @Counted(value = "booking.order.created", description = "Total booking orders creadas")
    @Timed(value = "booking.order.creation_time", description = "Tiempo en crear una booking order")
    public UUID createBookingOrder(UUID roomId, String customerName, LocalDate checkIn, LocalDate checkOut) {
        Log.infof("Creating booking order for room %s", roomId);

        UUID bookingOrderId = UUID.randomUUID();

        BookingOrder order = new BookingOrder(
                bookingOrderId,
                roomId,
                customerName,
                checkIn,
                checkOut,
                BookingStatus.PENDING
        );

        Log.debugf("Booking order payload: %s", order);

        bookingOrderRepository.save(order);

        Log.infof("Booking order created with ID: %s", order.getId());

        Log.infof("Sending booking order created event for ID: %s", order.getId());

        bookingOrderEmitter.send(new BookingOrderCreatedEvent(
                order.getId(),
                roomId,
                customerName,
                checkIn,
                checkOut
        ));

        return bookingOrderId;
    }

    @Timed(value = "booking.order.fetch.all.time", description = "Tiempo en obtener todas las booking orders")
    public List<BookingOrder> getAllBookingOrders() {
        Log.info("Fetching all booking orders");

        return bookingOrderRepository.findAll();
    }

    @Transactional
    public BookingOrder getBookingOrderById(UUID bookingOrderId) {
        Log.info("Fetching booking order with ID: " + bookingOrderId);

        return bookingOrderRepository.findById(bookingOrderId)
                .map(order -> {
                    Log.info("Booking order found: " + order.getId());
                    return order;
                })
                .orElseThrow(() -> {
                    Log.warnf("Booking order not found with ID: %s", bookingOrderId);
                    return new BookingOrderNotFoundException(
                            "Booking order not found: " + bookingOrderId
                    );
                });
    }

    @Transactional
    public void updateBookingOrderStatus(UUID bookingOrderId, BookingStatus status) {
        Log.info("Updating status of booking order " + bookingOrderId + " to " + status.name());

        bookingOrderRepository.updateStatus(bookingOrderId, status.name());

        Log.info("Status updated successfully for booking order: " + bookingOrderId);
    }
}
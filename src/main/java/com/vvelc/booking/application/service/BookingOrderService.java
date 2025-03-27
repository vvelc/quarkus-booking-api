package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderCreated;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
    Emitter<BookingOrderCreated> bookingOrderEmitter;

    private final BookingOrderRepository bookingOrderRepository;

    public UUID createBookingOrder(UUID roomId, String customerName, LocalDate checkIn, LocalDate checkOut) {
        UUID bookingOrderId = UUID.randomUUID();

        BookingOrder order = new BookingOrder(
                UUID.randomUUID(),
                roomId,
                customerName,
                checkIn,
                checkOut,
                BookingStatus.PENDING
        );

        bookingOrderRepository.save(order);

        bookingOrderEmitter.send(new BookingOrderCreated(
                bookingOrderId,
                roomId,
                customerName,
                checkIn,
                checkOut
        ));

        return bookingOrderId;
    }

    public List<BookingOrder> getAllBookingOrders() {
        return bookingOrderRepository.findAll();
    }

    public BookingOrder getBookingOrderById(UUID bookingOrderId) {
        return bookingOrderRepository.findById(bookingOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Booking order not found"));
    }

    public void updateBookingOrderStatus(UUID bookingOrderId, BookingStatus status) {
        bookingOrderRepository.updateStatus(bookingOrderId, status.name());
    }
}
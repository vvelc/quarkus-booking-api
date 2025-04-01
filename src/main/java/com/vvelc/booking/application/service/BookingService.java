package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.exception.BookingNotFoundException;
import com.vvelc.booking.domain.model.Booking;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingRepository;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Transactional
    @Counted(value = "bookings_created", description = "Total de bookings confirmados")
    @Timed(value = "booking_creation_time", description = "Tiempo en registrar un booking confirmado")
    public void createFromConfirmedOrder(BookingOrderStatusEvent event, BookingOrder order) {
        Log.info("Creating booking for confirmed order ID: " + event.getBookingOrderId());

        Booking booking = new Booking(
                UUID.randomUUID(),
                order.getRoomId(),
                order.getCustomerName(),
                order.getCheckIn(),
                order.getCheckOut()
        );

        bookingRepository.save(booking);

        Log.info("Booking created successfully with ID: " + booking.getId());
    }

    @Transactional
    @Timed(value = "booking.fetch.time", description = "Tiempo en buscar un booking")
    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> {
                    Log.info("Booking found: " + booking.getId());
                    return booking;
                })
                .orElseThrow(() -> {
                    Log.error("Booking not found: " + bookingId);
                    return new BookingNotFoundException("Booking not found: " + bookingId);
                });
    }

    @Transactional
    @Timed(value = "booking.fetch.all.time", description = "Tiempo en obtener todos los bookings")
    public List<Booking> getAllBookings() {
        Log.info("Fetching all bookings");

        return bookingRepository.findAll();
    }
}

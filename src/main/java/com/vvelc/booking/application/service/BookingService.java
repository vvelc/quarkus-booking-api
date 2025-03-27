package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.model.Booking;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    @Counted(name = "bookings_created", description = "Total de bookings confirmados")
    @Timed(name = "booking_creation_time", description = "Tiempo en registrar un booking confirmado", unit = MetricUnits.MILLISECONDS)
    public void createFromConfirmedOrder(BookingOrderStatusEvent event, BookingOrder order) {
        Booking booking = new Booking(
                UUID.randomUUID(),
                order.getRoomId(),
                order.getCustomerName(),
                order.getCheckIn(),
                order.getCheckOut()
        );

        bookingRepository.save(booking);
    }

    @Timed(name = "booking_fetch_time", description = "Tiempo en buscar un booking", unit = MetricUnits.MILLISECONDS)
    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}

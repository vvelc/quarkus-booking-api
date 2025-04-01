package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.vvelc.booking.domain.exception.BookingNotFoundException;
import com.vvelc.booking.domain.model.Booking;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BookingServiceTest {

    @InjectMock
    BookingRepository bookingRepository;

    @Inject
    BookingService bookingService;

    @Test
    void shouldCreateBookingFromConfirmedOrder() {
        // Given a confirmed booking order event and the corresponding BookingOrder
        UUID orderId = UUID.randomUUID();
        BookingOrder confirmedOrder = new BookingOrder(orderId, UUID.randomUUID(), "Carlos",
                LocalDate.now(), LocalDate.now().plusDays(5), null);
        BookingOrderStatusEvent event = new BookingOrderStatusEvent(orderId, null, "Confirmed");

        bookingService.createFromConfirmedOrder(event, confirmedOrder);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        Mockito.verify(bookingRepository).save(bookingCaptor.capture());
        Booking savedBooking = bookingCaptor.getValue();
        assertEquals(confirmedOrder.getRoomId(), savedBooking.getRoomId());
        assertEquals(confirmedOrder.getCustomerName(), savedBooking.getCustomerName());
        assertEquals(confirmedOrder.getCheckIn(), savedBooking.getCheckIn());
        assertEquals(confirmedOrder.getCheckOut(), savedBooking.getCheckOut());

        assertNotNull(savedBooking.getId());
        assertNotEquals(orderId, savedBooking.getId());
    }

    @Test
    void shouldReturnBookingWhenFoundById() {
        UUID bookingId = UUID.randomUUID();
        Booking booking = new Booking(bookingId, UUID.randomUUID(), "Diana",
                LocalDate.now(), LocalDate.now().plusDays(2));
        Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(bookingId);

        Mockito.verify(bookingRepository).findById(bookingId);
        assertSame(booking, result);
    }

    @Test
    void shouldThrowBookingNotFoundExceptionWhenNotExists() {
        UUID missingId = UUID.randomUUID();
        Mockito.when(bookingRepository.findById(missingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(missingId));
        Mockito.verify(bookingRepository).findById(missingId);
    }

    @Test
    void shouldReturnAllBookingsList() {
        Booking dummyBooking = new Booking(UUID.randomUUID(), UUID.randomUUID(), "Eve",
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(3));
        List<Booking> dummyList = List.of(dummyBooking);
        Mockito.when(bookingRepository.findAll()).thenReturn(dummyList);

        List<Booking> result = bookingService.getAllBookings();

        Mockito.verify(bookingRepository).findAll();
        assertEquals(dummyList, result);
        assertFalse(result.isEmpty());
        assertEquals(dummyBooking, result.getFirst());
    }
}

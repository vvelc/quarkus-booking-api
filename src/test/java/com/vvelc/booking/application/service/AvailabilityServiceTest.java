package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.event.BookingOrderCreatedEvent;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import com.vvelc.booking.domain.repository.BookingRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.UUID;


@QuarkusTest
class AvailabilityServiceTest {

    @InjectMock
    BookingRepository bookingRepository;

    @InjectMock
    BookingOrderRepository bookingOrderRepository;

    @Inject
    AvailabilityService availabilityService;

    @Test
    void shouldHandleConflictAndUpdateStatus() {
        var event = new BookingOrderCreatedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Client",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        Mockito.when(bookingRepository.hasOverlappingBooking(
                        event.getRoomId(), event.getCheckIn(), event.getCheckOut()))
                .thenReturn(true);

        availabilityService.checkAvailabilityAndRespond(event);

        Mockito.verify(bookingOrderRepository)
                .updateStatus(event.getBookingOrderId(), BookingStatus.REJECTED.name());
    }

    @Test
    void shouldHandleAvailabilityAndUpdateStatus() {
        var event = new BookingOrderCreatedEvent(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Client",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        Mockito.when(bookingRepository.hasOverlappingBooking(
                        event.getRoomId(), event.getCheckIn(), event.getCheckOut()))
                .thenReturn(false);

        availabilityService.checkAvailabilityAndRespond(event);

        Mockito.verify(bookingOrderRepository)
                .updateStatus(event.getBookingOrderId(), BookingStatus.CONFIRMED.name());
    }

}

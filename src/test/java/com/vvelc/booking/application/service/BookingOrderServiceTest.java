package com.vvelc.booking.application.service;

import com.vvelc.booking.domain.common.BookingStatus;
import com.vvelc.booking.domain.exception.BookingOrderNotFoundException;
import com.vvelc.booking.domain.model.BookingOrder;
import com.vvelc.booking.domain.repository.BookingOrderRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@QuarkusTest
class BookingOrderServiceTest {

    @InjectMock
    BookingOrderRepository bookingOrderRepository;

    @Inject
    BookingOrderService service;

    @Test
    void shouldSaveBookingOrder() {
        UUID roomId = UUID.randomUUID();
        String customerName = "John Doe";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(2);

        service.createBookingOrder(roomId, customerName, checkIn, checkOut);

        ArgumentCaptor<BookingOrder> captor = ArgumentCaptor.forClass(BookingOrder.class);
        Mockito.verify(bookingOrderRepository).save(captor.capture());

        BookingOrder saved = captor.getValue();
        Assertions.assertEquals(roomId, saved.getRoomId());
        Assertions.assertEquals(customerName, saved.getCustomerName());
        Assertions.assertEquals(checkIn, saved.getCheckIn());
        Assertions.assertEquals(checkOut, saved.getCheckOut());
        Assertions.assertEquals(BookingStatus.PENDING, saved.getStatus());
    }

    @Test
    void shouldReturnBookingOrderById() {
        UUID bookingOrderId = UUID.randomUUID();
        BookingOrder order = new BookingOrder(
                bookingOrderId,
                UUID.randomUUID(),
                "Client",
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                BookingStatus.PENDING
        );

        Mockito.when(bookingOrderRepository.findById(bookingOrderId)).thenReturn(Optional.of(order));

        BookingOrder result = service.getBookingOrderById(bookingOrderId);

        Assertions.assertEquals(order, result);
    }

    @Test
    void shouldThrowIfBookingOrderNotFound() {
        UUID bookingOrderId = UUID.randomUUID();
        Mockito.when(bookingOrderRepository.findById(bookingOrderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(BookingOrderNotFoundException.class, () -> {
            service.getBookingOrderById(bookingOrderId);
        });
    }

    @Test
    void shouldUpdateStatus() {
        UUID bookingOrderId = UUID.randomUUID();
        BookingStatus newStatus = BookingStatus.CONFIRMED;

        service.updateBookingOrderStatus(bookingOrderId, newStatus);

        Mockito.verify(bookingOrderRepository).updateStatus(bookingOrderId, newStatus.name());
    }
}

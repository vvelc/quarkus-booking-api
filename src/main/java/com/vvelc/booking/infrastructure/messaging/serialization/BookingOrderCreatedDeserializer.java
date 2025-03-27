package com.vvelc.booking.infrastructure.messaging.serialization;

import com.vvelc.booking.domain.event.BookingOrderCreated;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BookingOrderCreatedDeserializer extends ObjectMapperDeserializer<BookingOrderCreated> {
    public BookingOrderCreatedDeserializer() {
        super(BookingOrderCreated.class);
    }
}

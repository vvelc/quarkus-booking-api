package com.vvelc.booking.infrastructure.messaging.serialization;

import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class BookingOrderStatusEventDeserializer extends ObjectMapperDeserializer<BookingOrderStatusEvent> {
    public BookingOrderStatusEventDeserializer() {
        super(BookingOrderStatusEvent.class);
    }
}

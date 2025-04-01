package com.vvelc.booking.infrastructure.messaging.serialization;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vvelc.booking.domain.event.BookingOrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class BookingOrderCreatedEventDeserializer implements Deserializer<BookingOrderCreatedEvent> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public BookingOrderCreatedEventDeserializer() {
        // Constructor vacío necesario para Kafka
    }

    @Override
    public BookingOrderCreatedEvent deserialize(String topic, byte[] data) {
        try {
            return OBJECT_MAPPER.readValue(data, BookingOrderCreatedEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing BookingOrderCreatedEvent", e);
        }
    }
}

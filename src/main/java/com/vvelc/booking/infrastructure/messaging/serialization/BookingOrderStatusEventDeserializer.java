package com.vvelc.booking.infrastructure.messaging.serialization;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vvelc.booking.domain.event.BookingOrderStatusEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class BookingOrderStatusEventDeserializer implements Deserializer<BookingOrderStatusEvent> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public BookingOrderStatusEventDeserializer() {
        // Constructor vacío necesario para Kafka
    }

    @Override
    public BookingOrderStatusEvent deserialize(String topic, byte[] data) {
        try {
            return OBJECT_MAPPER.readValue(data, BookingOrderStatusEvent.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing BookingOrderStatusEvent", e);
        }
    }
}

package com.vvelc.booking.infrastructure.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class BookingEntity {

    @Id
    private UUID id;

    private UUID roomId;
    private String customerName;
    private LocalDate checkIn;
    private LocalDate checkOut;
}
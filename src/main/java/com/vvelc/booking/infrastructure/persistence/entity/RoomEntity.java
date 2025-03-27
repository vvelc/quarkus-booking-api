package com.vvelc.booking.infrastructure.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
public class RoomEntity {

    @Id
    private UUID id;

    @Column(unique = true)
    private String number;
}

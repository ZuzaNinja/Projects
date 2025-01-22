package org.example.monitoringandcommunication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
public class HourlyConsumption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID deviceId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private double consumption;
}

package org.example.monitoringandcommunication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class DeviceChanges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BINARY(16)")
    private UUID deviceId;

    @Column(nullable = false)
    private String changeType;

    @Column(nullable = false)
    private LocalDateTime changeTimestamp;

    @Column
    private String description;

}

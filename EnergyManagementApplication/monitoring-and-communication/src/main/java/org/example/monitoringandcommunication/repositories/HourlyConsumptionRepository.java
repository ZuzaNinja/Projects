package org.example.monitoringandcommunication.repositories;

import org.example.monitoringandcommunication.entities.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, UUID> {
    List<HourlyConsumption> findByDeviceIdAndTimestampBetween(UUID deviceId, LocalDateTime start, LocalDateTime end);
}


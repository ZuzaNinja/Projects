package org.example.monitoringandcommunication.services;

import org.example.monitoringandcommunication.dto.HourlyConsumptionDTO;
import org.example.monitoringandcommunication.repositories.HourlyConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class HourlyConsumptionService {

    @Autowired
    private HourlyConsumptionRepository repository;

    public List<HourlyConsumptionDTO> getHourlyConsumptionByDate(UUID deviceId, String date) {
        LocalDate selectedDate = LocalDate.parse(date);

        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.atTime(23, 59, 59);

        return repository.findByDeviceIdAndTimestampBetween(deviceId, start, end)
                .stream()
                .map(consumption -> new HourlyConsumptionDTO(
                        consumption.getTimestamp().getHour(),
                        consumption.getConsumption()))
                .toList();
    }
}

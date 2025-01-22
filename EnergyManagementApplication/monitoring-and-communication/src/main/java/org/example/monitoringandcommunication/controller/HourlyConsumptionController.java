package org.example.monitoringandcommunication.controller;

import org.example.monitoringandcommunication.dto.HourlyConsumptionDTO;
import org.example.monitoringandcommunication.services.HourlyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consumption")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:80"})
public class HourlyConsumptionController {

    @Autowired
    private HourlyConsumptionService hourlyConsumptionService;

    @GetMapping("/{deviceId}/{date}")
    public ResponseEntity<List<HourlyConsumptionDTO>> getHourlyConsumptionByDate(
            @PathVariable UUID deviceId,
            @PathVariable String date) {
        List<HourlyConsumptionDTO> consumptionData = hourlyConsumptionService.getHourlyConsumptionByDate(deviceId, date);
        return ResponseEntity.ok(consumptionData);
    }

}

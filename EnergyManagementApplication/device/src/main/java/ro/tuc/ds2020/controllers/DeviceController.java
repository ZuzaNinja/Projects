package ro.tuc.ds2020.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceAssignmentDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.services.DeviceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:80"})
public class DeviceController {

    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    private final DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Fetch a specific device by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable UUID id) {
        DeviceDTO device = deviceService.findDeviceById(id);
        return new ResponseEntity<>(device, HttpStatus.OK);
    }


    /**
     * Fetch all devices.
     */
    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> devices = deviceService.findAllDevices();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByUserId(@PathVariable UUID userId) {
        List<Device> devices = deviceRepository.findByUserId(userId);

        if (devices.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(device -> new DeviceDTO(
                        device.getId(),
                        device.getDescription(),
                        device.getAddress(),
                        device.getMaxHourlyConsumption(),
                        device.getUser().getId()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(deviceDTOs);
    }

    /**
     * Create a new device.
     */
    @PostMapping
    public ResponseEntity<UUID> createDevice(@Valid @RequestBody DeviceDetailsDTO deviceDetailsDTO) {
        UUID deviceId = deviceService.createDevice(deviceDetailsDTO);
        logger.info("Device created with ID: {}", deviceId);
        return new ResponseEntity<>(deviceId, HttpStatus.CREATED);
    }

    /**
     * Update a specific device.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDevice(@PathVariable UUID id, @Valid @RequestBody DeviceDetailsDTO deviceDetailsDTO) {
        deviceService.updateDevice(id, deviceDetailsDTO);
        logger.info("Device updated with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Delete a specific device by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id) {
        deviceService.deleteDevice(id);
        logger.info("Device deleted with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/assignDeviceToUser")
    public ResponseEntity<String> assignDeviceToUser(@RequestBody DeviceAssignmentDTO assignmentDTO) {
        deviceService.assignDeviceToUser(assignmentDTO.getDeviceId(), assignmentDTO.getUserId());
        return ResponseEntity.ok("Device assigned to user successfully.");
    }


    @GetMapping("/{id}/max-consumption")
    public ResponseEntity<Double> getMaxConsumption(@PathVariable UUID id) {
        Device device = deviceService.findDeviceEntityById(id);
        return ResponseEntity.ok(device.getMaxHourlyConsumption());
    }
}

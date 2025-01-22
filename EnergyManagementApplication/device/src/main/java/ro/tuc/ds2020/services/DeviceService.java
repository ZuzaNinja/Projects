package ro.tuc.ds2020.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.DeviceDetailsDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.messaging.DeviceEventPublisher;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    private DeviceEventPublisher deviceEventPublisher;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public UUID createDevice(DeviceDetailsDTO deviceDetailsDTO) {
        Device device = new Device(
                deviceDetailsDTO.getDescription(),
                deviceDetailsDTO.getAddress(),
                deviceDetailsDTO.getMaxHourlyConsumption()
        );

        if (deviceDetailsDTO.getUserId() != null) {
            User user = userRepository.findById(deviceDetailsDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + deviceDetailsDTO.getUserId()));
            device.setUser(user);
        }

        Device savedDevice = deviceRepository.save(device);

        deviceEventPublisher.publishDeviceEvent(savedDevice.getId(), "add");

        return savedDevice.getId();
    }

    public void updateDevice(UUID id, DeviceDetailsDTO deviceDetailsDTO) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
        device.setDescription(deviceDetailsDTO.getDescription());
        device.setAddress(deviceDetailsDTO.getAddress());
        device.setMaxHourlyConsumption(deviceDetailsDTO.getMaxHourlyConsumption());

        if (deviceDetailsDTO.getUserId() != null) {
            User user = userRepository.findById(deviceDetailsDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + deviceDetailsDTO.getUserId()));
            device.setUser(user);
        } else {
            device.setUser(null);
        }

        deviceRepository.save(device);

        deviceEventPublisher.publishDeviceEvent(device.getId(), "update");
    }

    public void deleteDevice(UUID id) {
        if (!deviceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Device not found with id: " + id);
        }
        deviceRepository.deleteById(id);

        deviceEventPublisher.publishDeviceEvent(id, "delete");
    }

    public void assignDeviceToUser(UUID deviceId, UUID userId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with ID: " + deviceId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        device.setUser(user);
        deviceRepository.save(device);

        deviceEventPublisher.publishDeviceEvent(deviceId, "assign");
    }

    public DeviceDTO findDeviceById(UUID id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));

        return DeviceBuilder.toDeviceDTO(device);
    }


    public List<DeviceDTO> findAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public Device findDeviceEntityById(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Device not found with id: " + id));
    }
}

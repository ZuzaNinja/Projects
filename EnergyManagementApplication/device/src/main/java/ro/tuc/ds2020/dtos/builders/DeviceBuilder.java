package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;

import java.util.UUID;

public class DeviceBuilder {

    public static DeviceDTO toDeviceDTO(Device device) {
        UUID userId = (device.getUser() != null) ? device.getUser().getId() : null;
        return new DeviceDTO(
                device.getId(),
                device.getDescription(),
                device.getAddress(),
                device.getMaxHourlyConsumption(),
                userId
        );
    }
}

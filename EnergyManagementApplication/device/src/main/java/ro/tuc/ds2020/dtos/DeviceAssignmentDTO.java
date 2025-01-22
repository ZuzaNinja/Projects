package ro.tuc.ds2020.dtos;

import java.util.UUID;

public class DeviceAssignmentDTO {

    private UUID userId;
    private UUID deviceId;

    public DeviceAssignmentDTO(UUID userId, UUID deviceId) {
        this.userId = userId;
        this.deviceId = deviceId;
    }


    public UUID getUserId() {
        return userId;
    }


    public UUID getDeviceId() {
        return deviceId;
    }

}

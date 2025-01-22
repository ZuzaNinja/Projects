package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class DeviceAssignmentDTO {

    private UUID userId;
    private UUID deviceId;


    public DeviceAssignmentDTO(UUID userId, UUID deviceId) {
        this.userId = userId;
        this.deviceId = deviceId;
    }

}

package ro.tuc.ds2020.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class LoginResponseDTO {
    private String role;
    private String redirectUrl;
    private UUID userId;
    private String token;
    private List<DeviceDTO> devices;

    public LoginResponseDTO(String role, String redirectUrl, UUID userId, String token, List<DeviceDTO> devices) {
        this.role = role;
        this.redirectUrl = redirectUrl;
        this.userId = userId;
        this.token = token;
        this.devices = devices;
    }
}

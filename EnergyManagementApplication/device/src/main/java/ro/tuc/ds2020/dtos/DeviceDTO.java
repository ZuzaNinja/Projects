package ro.tuc.ds2020.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class DeviceDTO {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("address")
    private String address;

    @JsonProperty("maxHourlyConsumption")
    private double maxHourlyConsumption;

    @JsonProperty("userId")
    private UUID userId;


    public DeviceDTO(UUID id, String description, String address, double maxHourlyConsumption, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.userId = userId;
    }
}

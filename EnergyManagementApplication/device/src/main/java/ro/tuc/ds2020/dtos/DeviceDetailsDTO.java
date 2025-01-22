package ro.tuc.ds2020.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class DeviceDetailsDTO {

    @NotNull
    private String description;

    @NotNull
    private String address;

    @NotNull
    private Double maxHourlyConsumption;

    private UUID userId;


    public DeviceDetailsDTO(String description, String address, Double maxHourlyConsumption, UUID userId) {
        this.description = description;
        this.address = address;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }


    public Double getMaxHourlyConsumption() {
        return maxHourlyConsumption;
    }

    public UUID getUserId() {
        return userId;
    }
}

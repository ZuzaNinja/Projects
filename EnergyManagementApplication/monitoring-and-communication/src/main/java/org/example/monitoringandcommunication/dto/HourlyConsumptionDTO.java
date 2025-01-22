package org.example.monitoringandcommunication.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HourlyConsumptionDTO {

    private int hour;
    private double energyValue;

    public HourlyConsumptionDTO(int hour, double energyValue) {
        this.hour = hour;
        this.energyValue = energyValue;
    }

}

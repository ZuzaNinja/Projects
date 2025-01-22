package org.example.monitoringandcommunication.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.monitoringandcommunication.entities.HourlyConsumption;
import org.example.monitoringandcommunication.repositories.HourlyConsumptionRepository;
import org.example.monitoringandcommunication.services.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@Component
public class MessageConsumer {

    @Autowired
    private HourlyConsumptionRepository repository;

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "consumptionQueue")
    public void handleConsumptionMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            UUID deviceId = UUID.fromString(jsonNode.get("device_id").asText());
            double hourlyConsumption = jsonNode.get("measurement_value").asDouble();
            long timestamp = jsonNode.get("timestamp").asLong();

            System.out.println("Received hourly consumption for device " + deviceId + ": " + hourlyConsumption);

            ZonedDateTime zdt = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.UTC);
            ZonedDateTime hourlyZonedTime = zdt.withMinute(0).withSecond(0).withNano(0);

            HourlyConsumption consumption = new HourlyConsumption();
            consumption.setDeviceId(deviceId);
            consumption.setTimestamp(hourlyZonedTime.toLocalDateTime());
            consumption.setConsumption(hourlyConsumption);

            repository.save(consumption);
            System.out.println("Saved consumption for device " + deviceId + ": " + hourlyConsumption);

            double maxAllowedConsumption = getDeviceMaxConsumption(deviceId);
            if (hourlyConsumption > maxAllowedConsumption) {
                System.out.println("Device " + deviceId + " exceeded max allowed consumption!");
                notificationService.checkAndNotify(deviceId, hourlyConsumption);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double getDeviceMaxConsumption(UUID deviceId) {
        try {
            //String url = "http://localhost:8081/api/devices/" + deviceId + "/max-consumption";
            String url = "http://reverse-proxy/device/api/devices/" + deviceId + "/max-consumption";
            RestTemplate restTemplate = new RestTemplate();
            Double maxConsumption = restTemplate.getForObject(url, Double.class);

            if (maxConsumption == null) {
                throw new IllegalArgumentException("Max consumption not found for device: " + deviceId);
            }

            return maxConsumption;
        } catch (Exception e) {
            System.err.println("Error retrieving max consumption for device " + deviceId + ": " + e.getMessage());
            return Double.MAX_VALUE;
        }
    }
}

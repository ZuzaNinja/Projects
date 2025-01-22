package org.example.monitoringandcommunication.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.monitoringandcommunication.config.RabbitMQConfig;
import org.example.monitoringandcommunication.entities.DeviceChanges;
import org.example.monitoringandcommunication.repositories.DeviceChangesRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DeviceEventConsumer {

    @Autowired
    private DeviceChangesRepository deviceChangesRepository;

    @RabbitListener(queues = RabbitMQConfig.DEVICE_CHANGES_QUEUE)
    public void handleDeviceChange(String message) {
        System.out.println("Received message: " + message);
        try {
            Thread.sleep(2000);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            UUID deviceId = UUID.fromString(jsonNode.get("deviceId").asText());
            String changeType = jsonNode.get("changeType").asText();
            LocalDateTime timestamp = LocalDateTime.parse(jsonNode.get("timestamp").asText());

            DeviceChanges deviceChange = new DeviceChanges();
            deviceChange.setDeviceId(deviceId);
            deviceChange.setChangeType(changeType);
            deviceChange.setChangeTimestamp(timestamp);

            switch (changeType.toLowerCase()) {
                case "add":
                    deviceChange.setDescription("Device added successfully.");
                    break;
                case "update":
                    deviceChange.setDescription("Device updated successfully.");
                    break;
                case "delete":
                    deviceChange.setDescription("Device deleted successfully.");
                    break;
                case "assign":
                    deviceChange.setDescription("Device assigned to a user.");
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported change type: " + changeType);
            }

            deviceChangesRepository.save(deviceChange);
            System.out.println("Processed device change: " + message);
        } catch (InterruptedException e) {
            System.err.println("Processing interrupted: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to process device change: " + e.getMessage());
        }
    }
}

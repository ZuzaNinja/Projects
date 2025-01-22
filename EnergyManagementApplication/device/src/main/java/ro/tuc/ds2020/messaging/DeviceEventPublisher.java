package ro.tuc.ds2020.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeviceEventPublisher {

    private static final String DEVICE_CHANGES_QUEUE = "deviceChangesQueue";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishDeviceEvent(UUID deviceId, String changeType) {
        try {
            String message = String.format(
                    "{\"deviceId\":\"%s\", \"changeType\":\"%s\", \"timestamp\":\"%s\"}",
                    deviceId, changeType, java.time.LocalDateTime.now()
            );

            System.out.println("Publishing to queue: " + DEVICE_CHANGES_QUEUE);
            rabbitTemplate.convertAndSend(DEVICE_CHANGES_QUEUE, message);
            System.out.println("Published message: " + message);
        } catch (Exception e) {
            System.err.println("Error publishing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


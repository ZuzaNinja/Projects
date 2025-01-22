package org.example.monitoringandcommunication.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DEVICE_CHANGES_QUEUE = "deviceChangesQueue";
    public static final String CONSUMPTION_QUEUE = "consumptionQueue";

    @Bean
    public Queue deviceChangesQueue() {
        return new Queue(DEVICE_CHANGES_QUEUE);
    }

    @Bean
    public Queue consumptionQueue() {
        return new Queue(CONSUMPTION_QUEUE);
    }
}

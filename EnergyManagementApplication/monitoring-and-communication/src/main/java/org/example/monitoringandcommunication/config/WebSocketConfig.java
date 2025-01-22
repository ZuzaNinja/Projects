package org.example.monitoringandcommunication.config;

import org.example.monitoringandcommunication.message.SocketHandler;
import org.example.monitoringandcommunication.services.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final NotificationService notificationService;

    public WebSocketConfig(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Bean
    public SocketHandler socketHandler() {
        return new SocketHandler(notificationService);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketHandler(), "/notifications").setAllowedOrigins("http://localhost:3000", "http://localhost:80");
    }

}

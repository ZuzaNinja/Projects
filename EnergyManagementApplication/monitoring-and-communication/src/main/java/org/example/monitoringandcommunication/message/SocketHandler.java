package org.example.monitoringandcommunication.message;

import org.example.monitoringandcommunication.services.NotificationService;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SocketHandler extends TextWebSocketHandler {

    private final NotificationService notificationService;

    public SocketHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket connection established with session ID: " + session.getId());
        notificationService.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket connection closed with session ID: " + session.getId());
        notificationService.removeSession(session);
    }

}



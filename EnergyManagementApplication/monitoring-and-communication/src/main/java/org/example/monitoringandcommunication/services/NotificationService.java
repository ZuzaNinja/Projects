package org.example.monitoringandcommunication.services;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void checkAndNotify(UUID deviceId, double hourlyConsumption) {
        String notificationMessage = "Device " + deviceId + " exceeded consumption limit! Hourly consumption: " + hourlyConsumption;
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(notificationMessage));
            } catch (IOException e) {
                System.err.println("Error sending notification: " + e.getMessage());
            }
        }
    }

}

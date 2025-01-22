package org.example.chat.controller.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.chat.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    private TokenValidationService tokenValidationService;

    public WebSocketHandler(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    private String extractToken(String query) {
        if (query == null) return null;

        String[] queryParams = query.split("&");

        for (String param : queryParams) {
            if (param.startsWith("token=")) {
                return param.substring(6);
            }
        }

        return null;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String userId = extractUserId(query);
        String token = extractToken(query);

        if (userId == null || token == null || !validateToken(token) || !validateUser(userId, token)) {
            System.err.println("Invalid user or token: " + userId);
            session.close(new CloseStatus(1008, "Unauthorized"));
            return;
        }

        System.out.println("WebSocket connection established for user: " + userId);
        sessions.put(userId, session);
    }



    private boolean validateToken(String token) {
        return tokenValidationService.validateToken(token);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = extractUserId(session.getUri().getQuery());
        if (userId != null) {
            sessions.remove(userId);
            System.out.println("Connection closed for user: " + userId);
            if ("admin".equals(userId)) {
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> chatMessage = objectMapper.readValue(message.getPayload(), Map.class);
        String senderId = (String) chatMessage.get("senderId");
        String receiverId = (String) chatMessage.get("receiverId");
        String messageType = (String) chatMessage.get("type");

        if (receiverId == null || senderId == null || messageType == null) {
            System.err.println("Invalid message received: " + chatMessage);
            return;
        }

        switch (messageType) {
            case "read-receipt":
                handleReadReceipt(chatMessage, receiverId);
                break;

            case "typing":
                handleTypingNotification(senderId, receiverId);
                break;

            default:
                handleChatMessage(chatMessage, senderId, receiverId);
                break;
        }
    }

    private void handleTypingNotification(String senderId, String receiverId) throws Exception {
        WebSocketSession receiverSession = sessions.get(receiverId);

        if (receiverSession != null && receiverSession.isOpen()) {
            Map<String, String> typingNotification = new HashMap<>();
            typingNotification.put("type", "typing");
            typingNotification.put("senderId", senderId);
            typingNotification.put("receiverId", receiverId);

            receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(typingNotification)));
            System.out.println("Typing notification sent from " + senderId + " to " + receiverId);
        } else {
            System.err.println("Typing notification failed: receiver session not available or closed.");
        }
    }



    private void handleChatMessage(Map<String, Object> chatMessage, String senderId, String receiverId) throws Exception {
        WebSocketSession receiverSession = sessions.get(receiverId);

        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            System.out.println("Message sent from " + senderId + " to " + receiverId);
        } else {
            System.err.println("Message delivery failed: receiver session not available or closed.");
        }
    }


    private void handleReadReceipt(Map<String, Object> chatMessage, String receiverId) throws Exception {
        String messageId = (String) chatMessage.get("messageId");
        if (messageId == null || messageId.isEmpty()) return;

        WebSocketSession receiverSession = sessions.get(receiverId);

        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
            System.out.println("Read receipt sent to " + receiverId + " for messageId: " + messageId);
        } else {
            System.err.println("Read receipt failed: Receiver session not available or closed.");
        }
    }


    private String extractUserId(String query) {
        if (query == null) return null;

        String[] queryParams = query.split("&");

        for (String param : queryParams) {
            if (param.startsWith("userId=")) {
                return param.substring(7);
            }
        }

        return null;
    }

    private boolean validateUser(String userId, String token) {
        try {
            //String url = "http://localhost:8080/api/auth/validate/" + userId;
            String url = "http://reverse-proxy/person/api/auth/validate/" + userId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> entity = new HttpEntity<>(null, headers);

            restTemplate.exchange(url, HttpMethod.GET, entity, Void.class);

            return true;
        } catch (Exception e) {
            System.err.println("User validation failed for userId: " + userId + ". Error: " + e.getMessage());
            return false;
        }
    }

}

package org.example.chat.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TokenValidationService {

    private final RestTemplate restTemplate;

    public TokenValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateToken(String token) {
        try {
            String url = "http://reverse-proxy/person/api/auth/validate";
            //String url = "http://localhost:8080/api/auth/validate";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> entity = new HttpEntity<>(null, headers);

            restTemplate.exchange(url, HttpMethod.GET, entity, Void.class);

            return true;
        } catch (Exception e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }
}

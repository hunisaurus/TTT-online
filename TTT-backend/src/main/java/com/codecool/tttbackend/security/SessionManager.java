package com.codecool.tttbackend.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class SessionManager {
    private Map<String, Long> sessions = new HashMap<>();

    public String createSession(Long userId) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, userId);
        return token;
    }
    public void invalidateSession(String token) {
        sessions.remove(token);
    }
}


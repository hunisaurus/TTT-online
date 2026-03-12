package com.codecool.tttbackend.dao.model;

import java.time.Instant;

public class RefreshToken {
    private int id;

    private int userId;

    private String tokenHash;

    private Instant expiresAt;

    private boolean revoked;

    private Instant createdAt;


    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

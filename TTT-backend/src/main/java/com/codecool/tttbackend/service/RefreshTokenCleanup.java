package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.RefreshTokenDAO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class RefreshTokenCleanup {

    private final RefreshTokenDAO refreshTokenDAO;

    public RefreshTokenCleanup(RefreshTokenDAO refreshTokenDAO) {
        this.refreshTokenDAO = refreshTokenDAO;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupExpiredTokens() {
        int deleted = refreshTokenDAO.deleteByExpiresAtBefore(Instant.now());
        System.out.println("Deleted " + deleted + " expired refresh tokens.");
    }
}

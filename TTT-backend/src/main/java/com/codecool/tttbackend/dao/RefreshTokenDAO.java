package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.RefreshToken;
import com.codecool.tttbackend.service.RefreshTokenService;

import java.time.Instant;

public interface RefreshTokenDAO {
    public void saveToken(RefreshToken refreshToken);
    public void updateToken(RefreshToken refreshToken);
    public RefreshToken findByHash(String hash);
    public RefreshToken findByRawToken(String token);
    public int deleteByExpiresAtBefore(Instant time);
}

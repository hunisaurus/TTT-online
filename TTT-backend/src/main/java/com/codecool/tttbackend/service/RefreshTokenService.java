package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.RefreshTokenDAO;
import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.model.RefreshToken;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.exception.BadRequestException;
import com.codecool.tttbackend.security.JwtUtil;
import com.codecool.tttbackend.security.TokenHashUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class RefreshTokenService {

    private final RefreshTokenDAO refreshTokenDAO;
    private final UserDAO userDAO;
    private final JwtUtil jwtUtil;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenDAO refreshTokenDAO, UserDAO userDAO, JwtUtil jwtUtil){
        this.refreshTokenDAO = refreshTokenDAO;
        this.userDAO = userDAO;
        this.jwtUtil = jwtUtil;
    }

    public String createRefreshToken(int userId){
        User user = userDAO.findUserById(userId);
        if (user == null) {
            throw new BadRequestException("Cannot create refresh token: user not found");
        }
        RefreshToken refreshToken = new RefreshToken();

        String token = jwtUtil.generateRefreshToken(user.getUsername());
        String tokenHash = TokenHashUtil.hash(token);

        refreshToken.setUserId(userId);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setRevoked(false);

        refreshTokenDAO.saveToken(refreshToken);

        return token;
    }
    public RefreshToken verifyToken(String token) {

        String hash = TokenHashUtil.hash(token);

        RefreshToken refreshToken = refreshTokenDAO.findByHash(hash);

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Token revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        return refreshToken;
    }

    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenDAO.updateToken(token);
    }
}

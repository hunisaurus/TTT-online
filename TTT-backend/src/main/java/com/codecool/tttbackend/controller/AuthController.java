package com.codecool.tttbackend.controller;

import com.codecool.tttbackend.controller.dto.request.LoginRequest;
import com.codecool.tttbackend.controller.dto.request.RefreshTokenRequest;
import com.codecool.tttbackend.controller.dto.request.RegisterRequest;
import com.codecool.tttbackend.controller.dto.response.AuthResponse;
import com.codecool.tttbackend.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public AuthController(AuthService authService){

        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);

        setRefreshTokenCookie(response, authResponse.getRefreshToken());

        authResponse.setRefreshToken(null);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);

        setRefreshTokenCookie(response, authResponse.getRefreshToken());

        authResponse.setRefreshToken(null);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setRefreshToken(refreshToken);

        AuthResponse authResponse = authService.refreshToken(refreshRequest);

        if (authResponse.getRefreshToken() != null) {
            setRefreshTokenCookie(response, authResponse.getRefreshToken());
        }

        authResponse.setRefreshToken(null);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // Prevents JavaScript access (XSS protection)
        cookie.setSecure(false); // Set to TRUE in production (HTTPS only), FALSE for local development
        cookie.setPath("/api/auth"); // Only send to auth endpoints
        cookie.setMaxAge((int) (refreshTokenExpiration / 1000)); // Convert to seconds
        // cookie.setSameSite("Strict"); // CSRF protection (requires Spring Boot 3.0+)
        response.addCookie(cookie);
    }

    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "refreshToken".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
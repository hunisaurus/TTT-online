package com.codecool.tttbackend.service;

import com.codecool.tttbackend.controller.dto.request.LoginRequest;
import com.codecool.tttbackend.controller.dto.request.RefreshTokenRequest;
import com.codecool.tttbackend.controller.dto.request.RegisterRequest;
import com.codecool.tttbackend.controller.dto.response.AuthResponse;
import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.security.JwtUtil;
import com.codecool.tttbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class AuthService {

    private final UserDAO userDAO;
    private final PasswordHasher passwordHasher;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthService(UserDAO userDAO, AuthenticationManager authManager, JwtUtil jwtUtil, @Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.userDAO = userDAO;
        this.passwordHasher = new PasswordHasher();
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userDAO.findByUsername(request.username()) != null) {
            throw new RuntimeException("Username already exists");
        }

        if (userDAO.findByEmail(request.email()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordHasher.hash(request.password()));
        user.setBirthDate(request.birthDate());
        user.setRegistrationDate(LocalDateTime.now());
        user.setRoles(Set.of("USER"));

        userDAO.addNewUser(user);

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new)
        );
    }

    public AuthResponse login(LoginRequest req) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        User user = userDAO.findByUsername(req.username());


        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(req.username());

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new)
        );
    }
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken, true)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken, true);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userDAO.findByUsername(username);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        String newAccessToken = jwtUtil.generateAccessToken(authentication);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new)
        );
    }
}

package com.codecool.tttbackend.service;

import com.codecool.tttbackend.controller.dto.request.LoginRequestDTO;
import com.codecool.tttbackend.controller.dto.request.RefreshTokenRequest;
import com.codecool.tttbackend.controller.dto.response.AuthResponseDTO;
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

@Service
public class AuthService {

    private final UserDAO userDAO;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthService(UserDAO userDAO, AuthenticationManager authManager, JwtUtil jwtUtil, @Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.userDAO = userDAO;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponseDTO login(LoginRequestDTO req) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );

        User user = userDAO.findByUsername(req.username());


        String accessToken = jwtUtil.generateAccessToken(authentication);
        String refreshToken = jwtUtil.generateRefreshToken(req.username());

        return new AuthResponseDTO(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new)
        );
    }
    public AuthResponseDTO refreshToken(RefreshTokenRequest request) {
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

        return new AuthResponseDTO(
                newAccessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new)
        );
    }
}

package com.codecool.tttbackend.service;

import com.codecool.tttbackend.controller.dto.AuthDTO;
import com.codecool.tttbackend.controller.dto.request.LoginRequestDTO;
import com.codecool.tttbackend.controller.dto.request.RefreshTokenRequest;
import com.codecool.tttbackend.controller.dto.request.RegisterRequestDTO;
import com.codecool.tttbackend.dao.UserDAO;

import com.codecool.tttbackend.dao.model.RefreshToken;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.exception.BadRequestException;
import com.codecool.tttbackend.exception.UnauthorizedException;
import com.codecool.tttbackend.security.JwtUtil;
import com.codecool.tttbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final UserDAO userDAO;
    private final PasswordHasher passwordHasher;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder encoder;

    public AuthService(
            UserDAO userDAO,
            AuthenticationManager authManager,
            JwtUtil jwtUtil,
            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
            RefreshTokenService refreshTokenService,
            PasswordEncoder encoder
    ) {
        this.userDAO = userDAO;
        this.refreshTokenService = refreshTokenService;
        this.passwordHasher = new PasswordHasher();
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
    }

    public AuthDTO register(RegisterRequestDTO request) {

        if (userDAO.findByUsername(request.username()) != null) {
            throw new BadRequestException("Username already exists");
        }

        if (userDAO.findByEmail(request.email()) != null) {
            throw new BadRequestException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordHasher.hash(request.password())); // hashed password
        user.setBirthDate(request.birthDate());
        user.setRegistrationDate(LocalDateTime.now());
        user.setRoles(Set.of("USER"));

        userDAO.addNewUser(user);

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        String accessToken = jwtUtil.generateAccessToken(auth);
        String refreshToken = refreshTokenService.createRefreshToken(user.getId()); // ensure saveToken() converts Instant → Timestamp

        return new AuthDTO(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> "ROLE_" + role)
                        .toArray(String[]::new)
        );
    }
    public AuthDTO login(LoginRequestDTO request) {
        String username = request.username();
        String password = request.password();

        User user = userDAO.findByUsername(username);
        if (user == null) {
            System.out.println("User not found in DB");
            throw new UnauthorizedException("Invalid credentials");
        }

        boolean passwordMatches = encoder.matches(password, user.getPasswordHash());
        if (!passwordMatches) {
            throw new UnauthorizedException("Invalid credentials");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        String accessToken = jwtUtil.generateAccessToken(auth);

        String refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthDTO(
                accessToken,
                refreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> "ROLE_" + role).toArray(String[]::new)
        );
    }
    public AuthDTO refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.getRefreshToken();

        RefreshToken token = refreshTokenService.verifyToken(refreshToken);

        User user = userDAO.findUserById(token.getUserId());

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        String newAccessToken = jwtUtil.generateAccessToken(authentication);

        refreshTokenService.revokeToken(token);

        String newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthDTO(
                newAccessToken,
                newRefreshToken,
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> "ROLE_" + role)
                        .toArray(String[]::new)
        );
    }
}

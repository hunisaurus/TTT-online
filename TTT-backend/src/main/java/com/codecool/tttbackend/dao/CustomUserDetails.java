package com.codecool.tttbackend.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetails(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {

        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE username = ? OR email = ?",
                (rs, rowNum) ->
                        User.builder()
                                .username(rs.getString("username"))
                                .password(rs.getString("password_hash"))
                                .roles("USER")
                                .build(),
                usernameOrEmail,
                usernameOrEmail
        );
    }
}

package com.codecool.tttbackend.controller.dto.request;

import java.time.LocalDate;

public record RegisterRequest(String email, String username, String password, LocalDate birthDate) {
}

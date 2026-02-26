package com.codecool.tttbackend.controller.dto.response;

import java.util.List;

public record LoginResponseDTO(String jwt, List<Integer> games) {
}

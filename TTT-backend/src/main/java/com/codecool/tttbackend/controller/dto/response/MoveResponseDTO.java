package com.codecool.tttbackend.controller.dto.response;

import java.util.List;

public record MoveResponseDTO(
    PlayerResponseDTO currentPlayer,
    List<String> activeBoards,
    PlayerResponseDTO winner,
    String userName,
    char character,
    int br, int bc,
    int sr, int sc) {
}

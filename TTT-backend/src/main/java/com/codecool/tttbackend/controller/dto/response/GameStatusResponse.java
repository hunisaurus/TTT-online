package com.codecool.tttbackend.controller.dto.response;

import java.util.List;

public record GameStatusResponse(List<Character> rotation, PlayerResponseDTO currentPlayer, String boardState, List<String> activeBoards){
}

package com.codecool.tttbackend.controller.dto.response;

import java.util.List;

public record GameStatusResponse(PlayerResponseDTO currentPlayer, String[][][][] smallBoards, String[][] bigBoard, List<String> activeBoards, PlayerResponseDTO winner){
}

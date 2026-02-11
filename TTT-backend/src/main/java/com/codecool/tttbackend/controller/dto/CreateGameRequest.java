package com.codecool.tttbackend.controller.dto;

import java.util.List;

public record CreateGameRequest (String userName, String gameName, int maxPlayerCount) {
}

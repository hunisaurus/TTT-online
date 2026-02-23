package com.codecool.tttbackend.controller.dto.request;

public record CreateGameRequest (String userName, String gameName, int maxPlayerCount) {
}

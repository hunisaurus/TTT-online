package com.codecool.tttbackend.controller.dto;

import com.codecool.tttbackend.dao.model.User;

public record JoinGameRequest (String userName, char character) {
}

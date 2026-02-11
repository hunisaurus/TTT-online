package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.dao.model.User;

import java.util.List;

public interface GameDAO {

    Game findGameById(int id);
    List<User> findUsersByGameId(int id);
    void addGame(Game game);
    void removeGame(Game game);
    void updateGame(Game game);
}

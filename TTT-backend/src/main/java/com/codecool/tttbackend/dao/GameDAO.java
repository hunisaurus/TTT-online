package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.dao.model.Player;

import java.util.List;

public interface GameDAO {

    Game findGameById(int id);
    List<Game> getAllGames();
    List<Player> findPlayersByGameId(int id);
    void addGame(Game game);
    void removeGame(Game game);
    void updateGame(Game game);
}

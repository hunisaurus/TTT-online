package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.dao.model.game.Player;
import com.codecool.tttbackend.dao.model.game.Position;

import java.util.List;

public interface GameDAO {

    Game findGameById(int id);
    List<Game> getAllGames();
    List<Player> findPlayersByGameId(int id);
    Player findPlayer(int gameId, int userId);
    void addGame(Game game);
    void removeGame(Game game);
    void updateGame(Game game);
    Position getActiveBoardByGameId(int id);
    List<Game> getAllGamesByUserId(int userId);
}

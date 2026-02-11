package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.GameDAO;
import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final GameDAO gameDAO;

    @Autowired
    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }

    public void createGame(String name){
        Game game = new Game();
        game.setName(name);
        game.setTimeCreated(LocalDateTime.now());
        game.setGameState("WAITING");

        gameDAO.addGame(game);
    }

    public void endGame(int id){
        Game game = gameDAO.findGameById(id);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + id);
        }
        gameDAO.removeGame(game);
    }

    public void joinGame(int id, User user){
        Game game = gameDAO.findGameById(id);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + id);
        }
        game.addUser(user);
        gameDAO.updateGame(game);
    }

    public void leaveGame(int id, User user){
        Game game = gameDAO.findGameById(id);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + id);
        }
        game.removeUser(user);
        gameDAO.updateGame(game);
    }

    public void updateGameState(int id, String gameState){
        Game game = gameDAO.findGameById(id);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + id);
        }
        game.setGameState(gameState);
        gameDAO.updateGame(game);
    }

    public List<Game> listAllGames() {
        return gameDAO.getAllGames();
    }
}


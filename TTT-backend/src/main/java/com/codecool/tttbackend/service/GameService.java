package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.GameDAO;
import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class GameService {

    private GameDAO gameDAO;

    @Autowired
    public GameService(GameDAO gameDAO){
        this.gameDAO = gameDAO;
    }

    public void createGame(String name){
        Game game = new Game();
        game.setName(name);
        game.setTimeCreated(LocalDateTime.now());

        gameDAO.addGame(game);
    }

    public void endGame(int id){
        gameDAO.removeGame(gameDAO.findGameById(id));
    }

    public void joinGame(int id, User user){
        Game game = gameDAO.findGameById(id);
        game.addUser(user);

        gameDAO.updateGame(game);
    }

    public void leaveGame(int id, User user){
        Game game = gameDAO.findGameById(id);
        game.removeUser(user);

        gameDAO.updateGame(game);
    }

}

package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.GameDAO;
import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.dao.model.GameState;
import com.codecool.tttbackend.dao.model.GameUser;
import com.codecool.tttbackend.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {

   private final GameDAO gameDAO;
   private final UserService userService;

   @Autowired
   public GameService(GameDAO gameDAO, UserService userService) {
      this.gameDAO = gameDAO;
      this.userService = userService;
   }

   public void createGame(String name) {
      Game game = new Game();
      game.setName(name);
      game.setTimeCreated(LocalDateTime.now());
      game.setGameState(GameState.WAITING);

      gameDAO.addGame(game);
   }

   public void endGame(int id) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }
      gameDAO.removeGame(game);
   }

   public void joinGame(int id, String userName, char character) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }

      GameUser gameUser = new GameUser();

      gameUser.setUser(userService.getUserByUserName(userName));
      gameUser.setCharacter(character);

      game.addUser(gameUser);
      gameDAO.updateGame(game);
   }

   public void leaveGame(int id, User user) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }

      List<GameUser> users = gameDAO.findUsersByGameId(game.getId());

      for(GameUser gameUser : users) {
         if (gameUser.getUser().getId().equals(user.getId())){
            game.removeUser(gameUser);
         }
      }

      gameDAO.updateGame(game);
   }

   public void updateGameState(int id, GameState gameState) {
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


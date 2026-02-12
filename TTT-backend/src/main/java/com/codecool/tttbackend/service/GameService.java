package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.GameDAO;
import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.dao.model.game.GameState;
import com.codecool.tttbackend.dao.model.game.Player;
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

   public void createGame(String creatorName, String gameName, int maxPlayers) {
      User creator = userService.getUserByUserName(creatorName);
      Game game = new Game();
      game.setCreator(creator);
      game.setName(gameName);
      game.setMaxPlayers(maxPlayers);
      game.setTimeCreated(LocalDateTime.now());
      game.setGameState(GameState.WAITING);

      gameDAO.addGame(game);
   }

   public void startGame(int id){
      Game game = gameDAO.findGameById(id);
      if (game == null){
         throw new IllegalArgumentException("Game not found: " + id);
      }
      game.setGameState(GameState.IN_PROGRESS);

      gameDAO.updateGame(game);
   }

   public void endGame(int id) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }
      game.setGameState(GameState.ENDED);

      gameDAO.updateGame(game);
   }

   public void joinGame(int id, String userName, char character) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }

      User user = userService.getUserByUserName(userName);
      if (user == null) {
         throw new IllegalArgumentException("User not found: " + userName);
      }

      Player player = new Player();
      player.setUser(user);
      player.setCharacter(character);

      game.addPlayer(player);
      gameDAO.updateGame(game);
   }


   public void leaveGame(int id, String userName) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }

      User user = userService.getUserByUserName(userName);

      List<Player> players = gameDAO.findPlayersByGameId(game.getId());

      for(Player player : players) {
         if (player.getUser().getId().equals(user.getId())){
            game.removePlayer(player);
         }
      }

      gameDAO.updateGame(game);
   }

   public void winGame(int id, String winnerName){
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }

      User user = userService.getUserByUserName(winnerName);

      Player winner = gameDAO.findPlayer(game.getId(), user.getId().intValue());

      game.setWinner(winner);

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


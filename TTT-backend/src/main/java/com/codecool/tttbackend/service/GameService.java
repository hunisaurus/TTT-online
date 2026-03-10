package com.codecool.tttbackend.service;

import com.codecool.tttbackend.controller.dto.request.CreateGameRequestDTO;
import com.codecool.tttbackend.controller.dto.response.GameResponseDTO;
import com.codecool.tttbackend.controller.dto.response.GameStatusResponseDTO;
import com.codecool.tttbackend.controller.dto.response.PlayerResponseDTO;
import com.codecool.tttbackend.dao.GameDAO;
import com.codecool.tttbackend.dao.model.game.*;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.domain.game.GameLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

   public int createGame(CreateGameRequestDTO createGameRequestDTO) {
      User creator = userService.getUserByUserName(createGameRequestDTO.userName());

      Player creatorPlayer = new Player();
      creatorPlayer.setUser(creator);
      creatorPlayer.setCharacter(createGameRequestDTO.character());

      Game game = new Game();
      game.addPlayer(creatorPlayer);
      game.setCreator(creator);
      game.setName(createGameRequestDTO.gameName());
      game.setMaxPlayers(createGameRequestDTO.maxPlayerCount());
      game.setTimeCreated(LocalDateTime.now());
      game.setGameState(GameState.WAITING);
      return gameDAO.addGame(game);
   }

   public void startGame(int id) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
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

      for (Player player : players) {
         if (player.getUser().getId() == user.getId()) {
            game.removePlayer(player);
         }
      }

      gameDAO.updateGame(game);
   }

   public void winGame(int id, String winnerName) {
      Game game = gameDAO.findGameById(id);
      if (game == null) {
         throw new IllegalArgumentException("Game not found: " + id);
      }

      User user = userService.getUserByUserName(winnerName);

      Player winner = gameDAO.findPlayer(game.getId(), user.getId());

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

   public GameStatusResponseDTO makeMove(int gameId, Move move) {
      Game game = gameDAO.findGameById(gameId);
      if (!GameLogic.validateMove(game, move)) return null;
      GameLogic.applyMove(game, move);
      GameLogic.setNextCurrentPlayer(game);
      GameLogic.setActiveBoardFromMove(move, game);
      gameDAO.updateGame(game);

      return getGameStatusResponseDTOFromGame(game);
   }

   public Player getPlayer(int gameId, String userName) {
      User user = userService.getUserByUserName(userName);
      Player player = gameDAO.findPlayer(gameId, user.getId());
      player.setUser(user);
      return player;
   }

   public List<GameResponseDTO> getUserGameResponseDTOs(String username) {
      User user = userService.getUserByUserName(username);
      if (user == null) return new ArrayList<>();

      return gameDAO
          .getAllGamesByUserId(
              user.getId())
          .stream()
          .map(this::getGameResponseDTOFromGame)
          .toList();
   }

   public List<GameResponseDTO> getAvailableGameResponseDTOs(String userName) {
      User user = userService.getUserByUserName(userName);
      return gameDAO.getAvailableGames().stream().filter(game -> game.getCreator().getId() != user.getId() && game.getPlayers().stream().noneMatch(player -> player.getUser().getId() == user.getId()) && game.getMaxPlayers() > game.getPlayers().size()).map(this::getGameResponseDTOFromGame).toList();
   }

   public List<GameResponseDTO> getActiveGameResponseDTOs(String userName) {
      User user = userService.getUserByUserName(userName);
      return gameDAO.getAllGamesByUserId(user.getId()).stream().filter(game -> game.getGameState().equals(GameState.IN_PROGRESS)).map(this::getGameResponseDTOFromGame).toList();
   }

   public List<GameResponseDTO> getUser(String userName) {
      User user = userService.getUserByUserName(userName);
      return gameDAO.getAllGamesByUserId(user.getId()).stream().filter(game -> game.getGameState().equals(GameState.IN_PROGRESS)).map(this::getGameResponseDTOFromGame).toList();
   }

   public GameStatusResponseDTO getGameStatus(int id) {
      Game game = gameDAO.findGameById(id);
      return getGameStatusResponseDTOFromGame(game);
   }

   private GameStatusResponseDTO getGameStatusResponseDTOFromGame(Game game) {
      boolean started = game != null && game.getGameState() == GameState.IN_PROGRESS;

      return new GameStatusResponseDTO(
          getPlayerResponseDTOFromPlayer(game.getCurrentPlayer()),
          game.getBoard().toSmallBoardsStrings(),
          game.getBoard().toBigBoardStrings(),
          getActiveBoardsFromGame(game),
          getPlayerResponseDTOFromPlayer(GameLogic.getWinningPlayer(game)),
          game.getRotation(),
          started
      );
   }

   private PlayerResponseDTO getPlayerResponseDTOFromPlayer(Player player) {
      if (player == null) return null;
      return new PlayerResponseDTO(
          player.getUser().getId(),
          player.getUser().getUsername(),
          player.getCharacter(),
          player.getNumberOfWins()
      );
   }

   private List<String> getActiveBoardsFromGame(Game game) {
      return game.getBoard().getActiveBoardPositions().stream().map(Position::toString).toList();
   }

   private GameResponseDTO getGameResponseDTOFromGame(Game game) {
      return new GameResponseDTO(
          game.getId(),
          game.getName(),
          game.getCreator().getUsername(),
          // change public and private logic later:
          "public",
          game.getMaxPlayers(),
          game.getPlayers().size(),
          game.getPlayers().stream().map(Player::getCharacter).toList()
      );
   }


}


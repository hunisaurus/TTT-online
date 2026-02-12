package com.codecool.tttbackend.dao.model.game;

import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.domain.game.BigBoard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

   private int id;
   private String name;
   private User creator;
   private List<Player> players;
   private LocalDateTime timeCreated;
   private GameState gameState;
   private Player currentPlayer;
   private int maxPlayers;
   private BigBoard board;
   private Player winner;

   public Game() {
      board = new BigBoard();
   }

   public void addPlayer(Player user) {
      if (players == null) {
         players = new ArrayList<>();
      }
      players.add(user);
   }

   public void removePlayer(Player player) {
      if (players != null) {
         players.removeIf(u -> u.getUser().getId() == (player.getUser().getId()));
      }
   }

   public String getName() {
      return name;
   }

   public GameState getGameState() {
      return gameState;
   }

   public List<Player> getPlayers() {
      return players;
   }

   public int getId() {
      return id;
   }

   public LocalDateTime getTimeCreated() {
      return timeCreated;
   }

   public User getCreator() {
      return creator;
   }

   public Player getCurrentPlayer() {
      return currentPlayer;
   }

   public int getMaxPlayers() {
      return maxPlayers;
   }

   public void setCreator(User creator) {
      this.creator = creator;
   }

   public void setCurrentPlayer(Player currentPlayer) {
      this.currentPlayer = currentPlayer;
   }

   public void setMaxPlayers(int maxPlayers) {
      this.maxPlayers = maxPlayers;
   }

   public void setPlayers(List<Player> players) {
      this.players = players;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setTimeCreated(LocalDateTime timeCreated) {
      this.timeCreated = timeCreated;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setGameState(GameState gameState) {
      this.gameState = gameState;
   }

   public BigBoard getBoard() {
      return board;
   }

   public void setBoard(BigBoard board) {
      this.board = board;
   }

   public void setWinner(Player winner) {
      this.winner = winner;
   }
}

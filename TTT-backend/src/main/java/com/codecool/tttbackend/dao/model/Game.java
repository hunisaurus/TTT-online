package com.codecool.tttbackend.dao.model;

import com.codecool.tttbackend.service.game.BigBoard;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {

   private int id;
   private String name;
   private User creator;
   private ArrayList<Player> players;
   private LocalDateTime timeCreated;
   private GameState gameState;
   private User currentPlayer;
   private int maxPlayers;
   private BigBoard board;

   public Game() {
      board = new BigBoard();
   }

   public void addUser(Player user) {
      if (players == null) {
         players = new ArrayList<>();
      }
      players.add(user);
   }

   public void removeUser(Player player) {
      if (players != null) {
         players.removeIf(u -> u.getUser().getId().equals(player.getUser().getId()));
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

   public void setPlayers(ArrayList<Player> players) {
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
}

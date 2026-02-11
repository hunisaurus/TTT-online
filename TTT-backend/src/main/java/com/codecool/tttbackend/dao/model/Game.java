package com.codecool.tttbackend.dao.model;

import com.codecool.tttbackend.service.game.BigBoard;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class Game {

   private int id;
   private String name;
   private User creator;
   private HashMap<User, Character> usersAndCharacters;
   private LocalDateTime timeCreated;
   private GameState gameState;
   private User currentPlayer;
   private int maxPlayers;
   private BigBoard board;

   public Game() {
      board = new BigBoard();
   }

   public void addUserAndCharacter(User user, char character) {
      if (usersAndCharacters == null) {
         usersAndCharacters = new HashMap<>();
      }
      usersAndCharacters.put(user, character);
   }

   public void removeUser(User user) {
      if (usersAndCharacters == null) throw new IllegalArgumentException("Cannot remove user: User is null!");
      if (!usersAndCharacters.containsKey(user)) throw new NullPointerException("Cannot remove user: There is no such user!");
      usersAndCharacters.remove(user);
   }

   public String getName() {
      return name;
   }

   public GameState getGameState() {
      return gameState;
   }

   public List<User> getUsers() {
      return usersAndCharacters.keySet().stream().toList();
   }

   public void setUsersAndCharacters(HashMap<User, Character> usersAndCharacters) {
      this.usersAndCharacters = usersAndCharacters;
   }

   public int getId() {
      return id;
   }

   public LocalDateTime getTimeCreated() {
      return timeCreated;
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

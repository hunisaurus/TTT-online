package com.codecool.tttbackend.dao.model;

import org.springframework.web.servlet.tags.ArgumentAware;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

   private int id;
   private String name;
   private User creator;
   private ArrayList<GameUser> gameUsers;
   private LocalDateTime timeCreated;
   private GameState gameState;
   private User currentPlayer;
   private int maxPlayers;

   public Game() {

   }

   public void addUser(GameUser user) {
      if (gameUsers == null) {
         gameUsers = new ArrayList<>();
      }
      gameUsers.add(user);
   }

   public void removeUser(GameUser gameUser) {
      if (gameUsers != null) {
         gameUsers.removeIf(u -> u.getUser().getId().equals(gameUser.getUser().getId()));
      }
   }

   public String getName() {
      return name;
   }

   public GameState getGameState() {
      return gameState;
   }

   public List<GameUser> getUsers() {
      return gameUsers;
   }

   public int getId() {
      return id;
   }

   public LocalDateTime getTimeCreated() {
      return timeCreated;
   }

   public void setUsers(ArrayList<GameUser> gameUsers) {
      this.gameUsers = gameUsers;
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
}

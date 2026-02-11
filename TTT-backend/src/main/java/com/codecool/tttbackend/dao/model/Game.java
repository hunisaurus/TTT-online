package com.codecool.tttbackend.dao.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Game {

   private int id;
   private String name;
   private User creator;
   private HashMap<User, Character> users;
   private LocalDateTime timeCreated;
   private GameState gameState;
   private User currentPlayer;
   private int maxPlayers;

   public Game() {

   }

   public void addUser(User user, char character) {
      if (users == null) {
         users = new HashMap<>();
      }
      users.put(user, character);
   }

   public void removeUser(User user) {
      if (users == null) throw new IllegalArgumentException("Cannot remove user: User is null!");
      if (!users.containsKey(user)) throw new NullPointerException("Cannot remove user: There is no such user!");
      users.remove(user);
   }

   public String getName() {
      return name;
   }

   public GameState getGameState() {
      return gameState;
   }

   public HashMap<User, Character> getUsers() {
      return users;
   }

   public int getId() {
      return id;
   }

   public LocalDateTime getTimeCreated() {
      return timeCreated;
   }

   public void setUsers(ArrayList<User> users) {
      this.users = users;
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

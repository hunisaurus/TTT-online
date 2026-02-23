package com.codecool.tttbackend.dao.model.game;

import com.codecool.tttbackend.dao.model.User;

public class Player {

    private User user;
    private Character character;
    private int numberOfWins;

    public Player(){
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public User getUser() {
        return user;
    }

    public Character getCharacter() {
        return character;
    }

   public int getNumberOfWins() {
      return numberOfWins;
   }

   public void setNumberOfWins(int numberOfWins) {
      this.numberOfWins = numberOfWins;
   }
}

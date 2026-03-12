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

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Player player = (Player) o;
      return user.getUsername().equals(player.getUser().getUsername());
   }
}

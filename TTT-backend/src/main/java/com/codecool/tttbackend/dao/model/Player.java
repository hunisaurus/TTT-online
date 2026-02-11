package com.codecool.tttbackend.dao.model;

public class Player {

    private User user;
    private Character character;

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
}

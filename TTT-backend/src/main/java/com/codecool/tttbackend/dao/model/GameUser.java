package com.codecool.tttbackend.dao.model;

public class GameUser {

    private User user;
    private Character character;

    public GameUser(){
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

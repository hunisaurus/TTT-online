package com.codecool.tttbackend.dao.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Game {

    private String name;
    private ArrayList<User> users;
    private int id;
    private LocalDateTime timeCreated;
    private String gameState;

    public Game(){

    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(User user){
        users.remove(user);
    }

    public String getName(){
        return name;
    }

    public String getGameState() {
        return gameState;
    }

    public ArrayList<User> getUsers() {
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

    public void setName(String name){
        this.name = name;
    }

    public void setGameState(String gameState){
        this.gameState = gameState;
    }
}

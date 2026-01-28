package com.codecool.tttbackend.dao.model;


public class User {

    private String email;
    private String username;
    private String passwordHash;
    private Long id;

    public User() {
    }

    public  void setEmail (String email) {
        this.email = email;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}

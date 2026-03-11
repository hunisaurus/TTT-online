package com.codecool.tttbackend.dao.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class User {

    private String email;
    private String username;
    private String passwordHash;
    private int id;
    private Set<String> roles = new HashSet<>();
    private LocalDateTime registrationDate;
    private LocalDate birthDate;
    private String profileImage;

    public User() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRegistrationDate(LocalDateTime date) {
        this.registrationDate = date;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public int getId() {
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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

package com.codecool.tttbackend.dao.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {

   private String email;
   private String username;
   private String passwordHash;
   private int id;
   private LocalDateTime registrationDate;
   private LocalDate birthDate;

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
}

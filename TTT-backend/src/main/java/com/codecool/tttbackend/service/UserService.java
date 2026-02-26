package com.codecool.tttbackend.service;

import com.codecool.tttbackend.controller.dto.request.RegisterRequest;
import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class UserService {

   private final UserDAO userDAO;
   private final PasswordHasher passwordHasher;

   @Autowired
   public UserService(UserDAO userDAO, PasswordHasher passwordHasher) {
      this.userDAO = userDAO;
      this.passwordHasher = passwordHasher;
   }

   public void register(RegisterRequest request){
      if (userDAO.findByUsername(request.username()) != null) {
         throw new RuntimeException("Username already exists");
      }

      if (userDAO.findByEmail(request.email()) != null) {
         throw new RuntimeException("Email already exists");
      }

      User user = new User();
      user.setUsername(request.username());
      user.setEmail(request.email());
      user.setPasswordHash(passwordHasher.hash(request.password()));
      user.setBirthDate(request.birthDate());
      user.setRegistrationDate(LocalDateTime.now());
      user.setRoles(Set.of("USER"));

      userDAO.addNewUser(user);
   }

   public User getUserByUserName(String userName){
      return userDAO.findByUsername(userName);
   }

   public User getUserById(int id){
      return userDAO.findUserById(id);
   }
}

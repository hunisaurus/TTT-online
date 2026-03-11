package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

   private final UserDAO userDAO;
   private final PasswordHasher passwordHasher;

   @Autowired
   public UserService(UserDAO userDAO, PasswordHasher passwordHasher) {
      this.userDAO = userDAO;
      this.passwordHasher = passwordHasher;
   }

   public User getUserByUserName(String userName){
      return userDAO.findByUsername(userName);
   }

   public User getUserById(int id){
      return userDAO.findUserById(id);
   }
}

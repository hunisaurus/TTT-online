package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.security.PasswordHasher;
import com.codecool.tttbackend.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

   private final UserDAO userDAO;
   private final PasswordHasher passwordHasher;
   private final SessionManager sessionManager;

   @Autowired
   public UserService(UserDAO userDAO) {
      this.userDAO = userDAO;
      this.passwordHasher = new PasswordHasher();
      this.sessionManager = new SessionManager();
   }

   public void register(String email, String username, String password, LocalDate birthDate) {
      if (userDAO.findByEmail(email) != null) {
         throw new RuntimeException("There is already a user with that email!");
      }

      User user = new User();
      user.setEmail(email);
      user.setUsername(username);
      user.setPasswordHash(passwordHasher.hash(password));
      user.setBirthDate(birthDate);

      userDAO.addNewUser(user);
   }

   public String login(String username, String password) {
      User user = userDAO.findByUsername(username);

      if (user == null || !passwordHasher.verify(password, user.getPasswordHash())) {
         throw new RuntimeException("Invalid username or password");
      }

//        return sessionManager.createSession(user.getId());

      // auth imitation with username (for now)
      return username;
   }

   public void logout(String token) {
      sessionManager.invalidateSession(token);
   }

   public User getUserByUserName(String userName){
      return userDAO.findByUsername(userName);
   }
}

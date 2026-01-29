package com.codecool.tttbackend.service;

import com.codecool.tttbackend.dao.UserDAO;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.security.PasswordHasher;
import com.codecool.tttbackend.security.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void register(String username, String password) {
        if (userDAO.findByUsername(username) != null) {
            throw new RuntimeException("Username is already in use");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHasher.hash(password));

        userDAO.addNewUser(user);
    }

    public String login(String username, String password) {
        User user = userDAO.findByUsername(username);

        if (user == null || !passwordHasher.verify(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid username or password");
        }

        return sessionManager.createSession(user.getId());
    }

    public void logout(String token) {
        sessionManager.invalidateSession(token);
    }
}

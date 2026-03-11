package com.codecool.tttbackend.service;

import com.codecool.tttbackend.controller.dto.request.RegisterRequestDTO;
import com.codecool.tttbackend.controller.dto.response.PlayerResponseDTO;
import com.codecool.tttbackend.dao.GameDAO;
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
    private final GameDAO gameDAO;

    @Autowired
    public UserService(UserDAO userDAO, PasswordHasher passwordHasher, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.passwordHasher = passwordHasher;
        this.gameDAO = gameDAO;
    }

    public User getUserByUserName(String userName) {
        return userDAO.findByUsername(userName);
    }

    public User getUserById(int id) {
        return userDAO.findUserById(id);
    }

    public PlayerResponseDTO getUserStats(String username) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null;
        }

        int idForStats = (int) user.getId();

        int wins = gameDAO.countWinsByUserId((int) user.getId());
        int totalGames = gameDAO.countTotalGamesByUserId((int) user.getId());

        return new PlayerResponseDTO(idForStats, user.getUsername(), ' ', wins, totalGames, user.getProfileImage());
    }
    public void updateProfileImage(String username, String base64Image) {
        User user = userDAO.findByUsername(username);

        if (user != null) {
            userDAO.updateProfileImage(user.getId(), base64Image);
        } else {
            throw new RuntimeException("User not found: " + username);
        }
    }

    public User findUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void deleteProfileImage(long userId) {
        userDAO.deleteProfileImage(userId);
    }
}

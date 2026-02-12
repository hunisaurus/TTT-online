package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.User;

public abstract interface UserDAO {

    User findByUsername(String username);
    User findByEmail(String email);
    User findUserById(int id);
    void addNewUser(User user);
    void updateUser (User user);
    void deleteUserById(int id);

}

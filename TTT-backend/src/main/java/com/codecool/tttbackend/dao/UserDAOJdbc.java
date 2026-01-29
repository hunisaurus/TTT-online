package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class UserDAOJdbc implements UserDAO {

   private final JdbcTemplate jdbcTemplate;

   public UserDAOJdbc(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   private RowMapper<User> userMapper = (rs, rowNum) -> {
      User u = new User();
      u.setId(rs.getLong("id"));
      u.setEmail("email");
      u.setUsername(rs.getString("username"));
      u.setPasswordHash(rs.getString("password_hash"));
      u.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
      u.setBirthDate(rs.getDate("birth_date").toLocalDate());
      return u;
   };

   @Override
   public User findByEmail(String email) {
      try {
         return jdbcTemplate.queryForObject(
             "SELECT * FROM users WHERE email = ?",
             userMapper,
             email
         );
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public User findByUsername(String username) {
      try {
         return jdbcTemplate.queryForObject(
             "SELECT * FROM users WHERE username = ?",
             userMapper,
             username
         );
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public User findUserById(Long id) {
      try {
         return jdbcTemplate.queryForObject(
             "SELECT * FROM users WHERE id = ?",
             userMapper,
             id
         );
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public void addNewUser(User user) {
      jdbcTemplate.update(
          "INSERT INTO users (email, username, password_hash, birth_date) VALUES (?, ?, ?, ?)",
          user.getEmail(),
          user.getUsername(),
          user.getPasswordHash(),
          user.getBirthDate()
      );
   }

   @Override
   public void updateUser(User user) {
      jdbcTemplate.update(
          "UPDATE users SET username = ?, password_hash = ?, email = ?, birth_date = ? WHERE id = ?",
          user.getEmail(),
          user.getUsername(),
          user.getPasswordHash(),
          user.getBirthDate(),
          user.getId()
      );
   }

   @Override
   public void deleteUserById(int id) {
      jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
   }
}

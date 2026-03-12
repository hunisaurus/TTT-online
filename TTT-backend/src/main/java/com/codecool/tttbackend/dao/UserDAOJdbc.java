package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public class UserDAOJdbc implements UserDAO {

   private final JdbcTemplate jdbcTemplate;

   public UserDAOJdbc(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
   }

   private RowMapper<User> userMapper = (rs, rowNum) -> {
      User u = new User();
      u.setId(rs.getInt("id"));
      u.setEmail(rs.getString("email"));
      u.setUsername(rs.getString("username"));
      u.setPasswordHash(rs.getString("password_hash"));
      u.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
      u.setBirthDate(rs.getDate("birth_date").toLocalDate());

      String[] rolesArray = (String[]) rs.getArray("roles").getArray();
      u.setRoles(Set.of(rolesArray));

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
   public User findUserById(int id) {
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
      String sql = """
        INSERT INTO users (username, email, password_hash, birth_date, registration_date)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id
    """;

      Integer id = jdbcTemplate.queryForObject(
              sql,
              new Object[] {
                      user.getUsername(),
                      user.getEmail(),
                      user.getPasswordHash(),
                      user.getBirthDate(),
                      user.getRegistrationDate()
              },
              Integer.class
      );

      user.setId(id);
   }

   @Override
   public void updateUser(User user) {
      jdbcTemplate.update(
              "INSERT INTO users (email, username, password_hash, birth_date, roles) VALUES (?, ?, ?, ?, ?)",
              user.getEmail(),
              user.getUsername(),
              user.getPasswordHash(),
              user.getBirthDate(),
              user.getRoles().toArray(new String[0])
      );
   }

    @Override
    public void deleteUserById(int id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public void updateProfileImage(long userId, String base64Image) {
        jdbcTemplate.update("UPDATE users SET profile_image = ? WHERE id = ?", base64Image, userId);
    }
    public void deleteProfileImage(long userId) {
        String sql = "UPDATE users SET profile_image = NULL WHERE id = ?";
        jdbcTemplate.update(sql, userId);
    }
}

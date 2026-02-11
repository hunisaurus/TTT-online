package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.Game;
import com.codecool.tttbackend.dao.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GameDAOJdbc implements GameDAO {

    private final JdbcTemplate jdbcTemplate;

    public GameDAOJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        u.setBirthDate(rs.getDate("birth_date").toLocalDate());
        return u;
    };

    private RowMapper<Game> gameMapper = (rs, rowNum) -> {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setGameState(rs.getString("game_state"));
        game.setName(rs.getString("name"));
        game.setTimeCreated(rs.getTimestamp("creation_date").toLocalDateTime());
        return game;
    };

    @Override
    public List<Game> getAllGames() {
        List<Game> games = jdbcTemplate.query("SELECT * FROM games", gameMapper);

        for (Game game : games) {
            game.setUsers(new ArrayList<>(findUsersByGameId(game.getId())));
        }

        return games;
    }

    @Override
    public List<User> findUsersByGameId(int gameId) {
        return jdbcTemplate.query(
                """
                SELECT u.*
                FROM users u
                JOIN game_users gu ON u.id = gu.user_id
                WHERE gu.game_id = ?
                """,
                userMapper,
                gameId
        );
    }

    @Override
    public Game findGameById(int id) {
        try {
            Game game = jdbcTemplate.queryForObject(
                    "SELECT * FROM games WHERE id = ?",
                    gameMapper,
                    id
            );

            if (game != null) {
                game.setUsers(new ArrayList<>(findUsersByGameId(game.getId())));
            }
            return game;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addGame(Game game) {
        String sql = "INSERT INTO games (name, time_created, game_state) VALUES (?, ?, ?) RETURNING id";

        Integer gameId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                game.getName(),
                Timestamp.valueOf(game.getTimeCreated()),
                game.getGameState()
        );

        if (gameId == null) return;

        game.setId(gameId);

        if (game.getUsers() != null) {
            String joinSql = "INSERT INTO game_users (game_id, user_id) VALUES (?, ?)";
            for (User user : game.getUsers()) {
                jdbcTemplate.update(joinSql, gameId, user.getId());
            }
        }
    }

    @Override
    public void updateGame(Game game) {
        String sql = "UPDATE games SET name = ?, game_state = ?, time_created = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                game.getName(),
                game.getGameState(),
                Timestamp.valueOf(game.getTimeCreated()),
                game.getId()
        );

        jdbcTemplate.update("DELETE FROM game_users WHERE game_id = ?", game.getId());

        if (game.getUsers() != null) {
            String joinSql = "INSERT INTO game_users (game_id, user_id) VALUES (?, ?)";
            for (User user : game.getUsers()) {
                jdbcTemplate.update(joinSql, game.getId(), user.getId());
            }
        }
    }

    @Override
    public void removeGame(Game game){
        jdbcTemplate.update("DELETE FROM games WHERE id = ?", game.getId());
    }
}
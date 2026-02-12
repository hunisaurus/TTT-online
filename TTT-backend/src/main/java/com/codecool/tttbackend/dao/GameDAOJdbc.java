package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.game.Game;
import com.codecool.tttbackend.dao.model.game.GameState;
import com.codecool.tttbackend.dao.model.game.Player;
import com.codecool.tttbackend.dao.model.User;
import com.codecool.tttbackend.dao.model.game.Position;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GameDAOJdbc implements GameDAO {

    private final JdbcTemplate jdbcTemplate;
    private UserDAO userDAO;

    public GameDAOJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDAO = new UserDAOJdbc(jdbcTemplate);
    }

    private RowMapper<Player> playerMapper = (rs, rowNum) -> {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRegistrationDate(rs.getTimestamp("registration_date").toLocalDateTime());
        u.setBirthDate(rs.getDate("birth_date").toLocalDate());

        Player gu = new Player();
        gu.setUser(u);
        gu.setCharacter(rs.getString("character").charAt(0));
        return gu;
    };

    private RowMapper<Game> gameMapper = (rs, rowNum) -> {
        Game game = new Game();
        game.setId(rs.getInt("id"));
        game.setName(rs.getString("name"));
        game.setCreator(userDAO.findUserById(rs.getLong("creator_id")));
        game.setMaxPlayers(rs.getInt("max_players"));
        game.setPlayers(findPlayersByGameId(rs.getInt("id")));
        game.setCurrentPlayer(userDAO.findUserById(rs.getLong("current_player")));
        game.setGameState(GameState.valueOf(rs.getString("game_state")));
        game.setTimeCreated(rs.getTimestamp("creation_date").toLocalDateTime());
        return game;
    };

    @Override
    public List<Game> getAllGames() {
        List<Game> games = jdbcTemplate.query("SELECT * FROM games", gameMapper);

        for (Game game : games) {
            game.setPlayers(new ArrayList<>(findPlayersByGameId(game.getId())));
        }

        return games;
    }

    @Override
    public List<Player> findPlayersByGameId(int gameId) {
        return jdbcTemplate.query(
                """
                SELECT u.*, p.character
                FROM users u
                JOIN players p ON u.id = p.user_id
                WHERE p.game_id = ?
                """,
                playerMapper,
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
                game.setPlayers(new ArrayList<>(findPlayersByGameId(game.getId())));
            }
            return game;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addGame(Game game) {
        String sql = "INSERT INTO games (name, creation_date, game_state, max_players, creator_id) VALUES (?, ?, ?, ?, ?) RETURNING id";

        Integer gameId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                game.getName(),
                Timestamp.valueOf(game.getTimeCreated()),
                game.getGameState().name(),
                game.getMaxPlayers(),
                game.getCreator().getId()
        );

        if (gameId == null) return;

        game.setId(gameId);

        if (game.getPlayers() != null) {
            String joinSql = "INSERT INTO players (game_id, user_id) VALUES (?, ?)";
            for (Player player : game.getPlayers()) {
                jdbcTemplate.update(joinSql, gameId, player.getUser().getId());
            }
        }
    }

    @Override
    public void updateGame(Game game) {
        String sql = "UPDATE games SET name = ?, game_state = ?, creation_date = ? WHERE id = ?";
        jdbcTemplate.update(
                sql,
                game.getName(),
                game.getGameState().name(),
                Timestamp.valueOf(game.getTimeCreated()),
                game.getId()
        );

        jdbcTemplate.update("DELETE FROM players WHERE game_id = ?", game.getId());

        if (game.getPlayers() != null) {
            String joinSql = "INSERT INTO players (game_id, user_id, character) VALUES (?, ?, ?)";
            for (Player player : game.getPlayers()) {
                jdbcTemplate.update(joinSql, game.getId(), player.getUser().getId(), player.getCharacter());
            }
        }
    }

    @Override
    public List<Position> getActiveBoardsByGameId(int id) {
        try {
            Game game = jdbcTemplate.queryForObject(
                "SELECT active_board FROM games WHERE id = ?",
                gameMapper,
                id
            );

            if (game != null) {
                game.setPlayers(new ArrayList<>(findPlayersByGameId(game.getId())));
            }
            return game;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void removeGame(Game game){
        jdbcTemplate.update("DELETE FROM games WHERE id = ?", game.getId());
    }
}
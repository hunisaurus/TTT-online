package com.codecool.tttbackend.dao;

import com.codecool.tttbackend.dao.model.RefreshToken;
import com.codecool.tttbackend.security.TokenHashUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;

@Repository
public class RefreshTokenDAOJdbc implements RefreshTokenDAO{

    private final JdbcTemplate jdbcTemplate;

    public RefreshTokenDAOJdbc(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveToken(RefreshToken refreshToken){
        String sql = "INSERT INTO refresh_token (token_hash, user_id, expires_at, revoked) VALUES (?, ?, ?, ?)";

        Timestamp expiresAt = Timestamp.from(refreshToken.getExpiresAt());

        jdbcTemplate.update(
                sql,
                refreshToken.getTokenHash(),
                refreshToken.getUserId(),
                expiresAt,
                refreshToken.isRevoked()
        );
    }

    @Override
    public void updateToken(RefreshToken refreshToken){
        jdbcTemplate.update(
                "UPDATE refresh_token SET token_hash = ?, user_id = ?, created_at = ?, expires_at = ?, revoked = ? WHERE id = ?",
                refreshToken.getTokenHash(),
                refreshToken.getUserId(),
                Timestamp.from(refreshToken.getCreatedAt()),
                Timestamp.from(refreshToken.getExpiresAt()),
                refreshToken.isRevoked(),
                refreshToken.getId()
        );
    }

    @Override
    public RefreshToken findByRawToken(String rawToken) {
        String hashedToken = TokenHashUtil.hash(rawToken);
        return jdbcTemplate.queryForObject(
                "SELECT * FROM refresh_token WHERE token_hash = ? AND revoked = false",
                new Object[]{hashedToken},
                (rs, rowNum) -> {
                    RefreshToken token = new RefreshToken();
                    token.setId(rs.getInt("id"));
                    token.setUserId(rs.getInt("user_id"));
                    token.setTokenHash(rs.getString("token_hash"));
                    token.setExpiresAt(rs.getTimestamp("expires_at").toInstant());
                    token.setRevoked(rs.getBoolean("revoked"));
                    token.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    return token;
                }
        );
    }

    @Override
    public RefreshToken findByHash(String hash){
        String sql = "SELECT * FROM refresh_token WHERE token_hash = ?";

        return jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> {
                    RefreshToken token = new RefreshToken();
                    token.setId(rs.getInt("id"));
                    token.setUserId(rs.getInt("user_id"));
                    token.setTokenHash(rs.getString("token_hash"));
                    token.setExpiresAt(rs.getTimestamp("expires_at").toInstant());
                    token.setRevoked(rs.getBoolean("revoked"));
                    token.setCreatedAt(rs.getTimestamp("created_at").toInstant());
                    return token;
                },
                hash
        );
    }

    public int deleteByExpiresAtBefore(Instant time) {

        String sql = """
        DELETE FROM refresh_token
        WHERE expires_at < ?
    """;

        return jdbcTemplate.update(sql, Timestamp.from(time));
    }
}

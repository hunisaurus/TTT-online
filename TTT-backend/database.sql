CREATE EXTENSION IF NOT EXISTS citext;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id                BIGSERIAL PRIMARY KEY,
    email             CITEXT    NOT NULL UNIQUE,
    username          CITEXT    NOT NULL UNIQUE,
    password_hash     TEXT      NOT NULL,
    birth_date        TIMESTAMP NOT NULL,
    registration_date DATE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE games (
    id SERIAL PRIMARY KEY,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    game_state VARCHAR(100) NOT NULL
);

CREATE TABLE game_users (
    game_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (game_id, user_id),
    FOREIGN KEY (game_id) REFERENCES games(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

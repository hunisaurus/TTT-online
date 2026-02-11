CREATE EXTENSION IF NOT EXISTS citext;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS games CASCADE;

CREATE TABLE users
(
    id                BIGSERIAL PRIMARY KEY,
    email             CITEXT    NOT NULL UNIQUE,
    username          CITEXT    NOT NULL UNIQUE,
    password_hash     TEXT      NOT NULL,
    birth_date        TIMESTAMP NOT NULL,
    registration_date DATE      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE games
(
    id             SERIAL PRIMARY KEY,
    creation_date  TIMESTAMP                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    creator_id     INTEGER REFERENCES users (id) NOT NULL,
    name           VARCHAR(255)                  NOT NULL,
    game_state     VARCHAR(100)                  NOT NULL CHECK (game_state IN ('WAITING', 'IN_PROGRESS', 'ENDED')),
    winner         INTEGER REFERENCES users (id),
    max_players    INTEGER,
    board_state    VARCHAR(100),
    current_player INTEGER REFERENCES users (id)
);

CREATE TABLE players
(
    game_id   INTEGER REFERENCES games (id) NOT NULL,
    user_id   INTEGER REFERENCES users (id) NOT NULL,
    character VARCHAR(1)                    NOT NULL,
    PRIMARY KEY (game_id, user_id)
);

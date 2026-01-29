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
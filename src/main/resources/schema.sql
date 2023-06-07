DROP TABLE IF EXISTS FRIENDS;
DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS FILM_GENRES;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS MPA;

CREATE TABLE IF NOT EXISTS USERS
(
    "ID"       integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "EMAIL"    varchar(30)        NOT NULL,
    "LOGIN"    varchar(30) UNIQUE NOT NULL,
    "NAME"     varchar(40)        NOT NULL,
    "BIRTHDAY" date
);

CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID   integer PRIMARY KEY,
    MPA_NAME varchar(50)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    "FILM_ID"      integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "NAME"         varchar(30) NOT NULL,
    "DESCRIPTION"  varchar(100),
    "RELEASE_DATE" date,
    "DURATION"     integer     NOT NULL,
    "MPA"          integer REFERENCES MPA (MPA_ID)
);

CREATE TABLE IF NOT EXISTS GENRES
(
    GENRE_ID int PRIMARY KEY,
    GENRE    varchar(100)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    FILM_ID  integer REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    GENRE_ID int REFERENCES GENRES (GENRE_ID),
    CONSTRAINT film_genres_pk PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    USER_ID bigint REFERENCES USERS (ID) ON DELETE CASCADE,
    FILM_ID bigint REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
    CONSTRAINT likes_pk PRIMARY KEY (USER_ID, FILM_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    user_id     integer REFERENCES USERS (id) ON DELETE CASCADE,
    friends_id  integer REFERENCES USERS (id) ON DELETE CASCADE,
    is_approved boolean,
    CONSTRAINT friendships_pk PRIMARY KEY (user_id, friends_id)
);
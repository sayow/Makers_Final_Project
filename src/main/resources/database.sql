-- -------------------------------------------------------------
-- TablePlus 5.3.6(496)
--
-- https://tableplus.com/
--
-- Database: belle_musica
-- Generation Time: 2023-04-25 11:31:23.3270
-- -------------------------------------------------------------
DROP TABLE IF EXISTS "public"."followers" CASCADE;
DROP SEQUENCE IF EXISTS followers_id_seq CASCADE;
CREATE SEQUENCE IF NOT EXISTS followers_id_seq;

DROP TABLE IF EXISTS "public"."likes" CASCADE;
DROP SEQUENCE IF EXISTS likes_id_seq CASCADE;
CREATE SEQUENCE IF NOT EXISTS likes_id_seq;

DROP TABLE IF EXISTS "public"."users" CASCADE;
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
CREATE SEQUENCE IF NOT EXISTS users_id_seq;

-- Table Definition
CREATE TABLE "public"."users" (
                                  "id" int4 PRIMARY KEY NOT NULL DEFAULT nextval('users_id_seq'::regclass),
                                  "username" varchar NOT NULL UNIQUE,
                                  "email" varchar NOT NULL UNIQUE,
                                  "encrypted_password" varchar NOT NULL,
                                  "profile_picture" varchar DEFAULT '/static/s.png'
);

-- Table Definition
CREATE TABLE "public"."likes" (
                                  "id" int4 PRIMARY KEY NOT NULL DEFAULT nextval('likes_id_seq'::regclass),
                                  "album_id" varchar NOT NULL,
                                  "user_id" int4 NOT NULL,
                                  CONSTRAINT fk_likes_user
                                      FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Table Definition
CREATE TABLE "public"."followers" (
                                  "id" int4 PRIMARY KEY NOT NULL DEFAULT nextval('followers_id_seq'::regclass),
                            -- the follower_id represents the user id of the user who follows another user
                                  "follower_id" int4 NOT NULL,
                            -- the followed_user_id represents the user id of the user who is being followed by another user.
                                  "followed_user_id" int4 NOT NULL,
                                  CONSTRAINT fk_follower
                                      FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
                                  CONSTRAINT fk_followed_user
                                      FOREIGN KEY (followed_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert random data into the "users" table
INSERT INTO users (username, email, encrypted_password, profile_picture)
VALUES
    ('sayow', 'synr@example.com' ,'P@ssword123.','/static/cedric.png'),
    ('mahmoud', 'mmd@example.com', 'P@ssword123.','/static/mahmoud.png'),
    ('paloma', 'plm@example.com' ,'P@ssword123.','/static/paloma.png'),
    ('tiago', 'tig@example.com', 'P@ssword123.','/static/tiago.png'),
    ('hallyson', 'hlys@example.com' ,'P@ssword123.','/static/hallyson.png'),
    ('miguel', 'mgl@example.com', 'P@ssword123.','/static/miguel.png'),
    ('cedric', 'cdc@example.com' ,'P@ssword123.', '/static/cedric.png'),
    ('mumtaz', 'mtz@example.com', 'P@ssword123.','/static/mumtaz.png'),
    ('dominique', 'dmnq@example.com','P@ssword123.','/static/dominique.png'),
    ('vanessa', 'vns@example.com', 'P@ssword123.','/static/vanessa.png');

-- Insert random data into the "likes" table
INSERT INTO likes (album_id, user_id)
VALUES
-- USER 1
    ('1szeFfGnmYeIBXjBQrojqm', 1),
    ('4poE5j5Kl7rlagyb7cLW5h', 1),
    ('0wkT2e3V1BNPfIgn9xMFS5', 1),
    ('7mBNxvIt8hHFRBV2Q5tMrH', 1),
    ('5jEsk2yJeuTTcp3fpjfIr4', 1),
-- USER 2
    ('2R9pasBQjxQRkudZTfxfJv', 2),
    ('3WkmeuxpkLGq8NtudY2nO1', 2),
    ('4YMGnN1EoyxrarAr0JOZzr', 2),
    ('1Fmi8IPxNjOp9FraQCX6BE', 2),
    ('4zqIP7G4nEeIzcy1ITm0e8', 2),
-- USER 3
    ('6xStbHr2tsvtTuE5xiw8R0', 3),
    ('6IoSklpE8MuUV7YuPGmA6e', 3),
    ('4g7Eb2jA0kxUZRhdEmBjdX', 3),
    ('0gwcU06PhVSrqVsn3SGE8K', 3),
    ('4zqIP7G4nEeIzcy1ITm0e8', 3),
-- USER 3
    ('3aCB0VEkNCxcr04H6vYXVS', 4),
    ('1GTfPxkfgvvPvAi0YJJ5Lu', 4),
    ('0aldvxsLElfOrjrH3ufVz8', 4),
    ('1DnoMTq3rkXEhFdRAv8dVu', 4),
    ('6xStbHr2tsvtTuE5xiw8R0', 4),
-- USER 5
    ('2R9pasBQjxQRkudZTfxfJv', 5),
    ('7IoLC1fGqp1uefNwM2i862', 5),
    ('2xCxSDKukJV6o4r1dDyvd3', 5),
    ('2uiVux6Q24aN6PzF8uRG7O', 5),
    ('3DEIbxPvQqU7wSTQO9aG9A', 5),
-- USER 6
    ('3vuXCf8U5bM9t5oL0WCEkn', 6),
    ('1fgliWqQUF7q7Re8dfRXl6', 6),
    ('5gDefZjQUjJIn8Zxjeo5Jr', 6),
    ('5jEsk2yJeuTTcp3fpjfIr4', 6),
    ('37kw8Fn2JQE2GJ5W4CB0Af', 6),
-- USER 7
    ('0tjIjjJIJbUKGcQFTIlXWM', 7),
    ('5RR3LX8sRWE771Merr2wA3', 7),
    ('7fFl1TABviXjPPBEdCOwqw', 7),
    ('7mBNxvIt8hHFRBV2Q5tMrH', 7),
    ('14sG6gyvYAOlliXg1uhr7E', 7),
-- USER 8
    ('4T0wElZm8RXLsk5ZI0ZGxY', 8),
    ('41uVl8F0aF71LrKctKxXHi', 8),
    ('5Xv45jxK6sH2jVISOe8sTH', 8),
    ('0wkT2e3V1BNPfIgn9xMFS5', 8),
    ('29VuFjH8b0MmLmRPeKb1cK', 8),
-- USER 9
    ('4T0wElZm8RXLsk5ZI0ZGxY', 9),
    ('5Xv45jxK6sH2jVISOe8sTH', 9),
    ('5Xv45jxK6sH2jVISOe8sTH', 9),
    ('0wkT2e3V1BNPfIgn9xMFS5', 9),
    ('29VuFjH8b0MmLmRPeKb1cK', 9),
-- USER 10
    ('1t3gqZG4owzK59lurQyRjl', 10),
    ('6PWZmDj0bqiOqjMANAyjvH', 10),
    ('1gDwk7oVBRAUiVFtMsrSM5', 10),
    ('3HdS3puSud9A3sfksnfb74', 10),
    ('0fz9O5VLlPN6gxypI3Pobx', 10);

-- Insert random data into the "followers" table
INSERT INTO followers (follower_id, followed_user_id)
VALUES
-- USER 1
    (2,1),
    (3,1),
    (4,1),
-- USER 2
    (5,2),
    (6,2),
    (7,2),
-- USER 3
    (8,3),
    (9,3),
    (10,3),
-- USER 4
    (1,4),
    (2,4),
    (3,4),
-- USER 5
    (4,5),
    (6,5),
    (7,5),
-- USER 6
    (8,6),
    (9,6),
    (10,6),
-- USER 7
    (1,7),
    (2,7),
    (3,7),
-- USER 8
    (4,8),
    (5,8),
    (6,8),
-- USER 9
    (8,9),
    (9,9),
    (10,9),
-- USER 10
    (1,10),
    (2,10),
    (3,10);




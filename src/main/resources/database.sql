-- -------------------------------------------------------------
-- TablePlus 5.3.6(496)
--
-- https://tableplus.com/
--
-- Database: belle_musica
-- Generation Time: 2023-04-25 11:31:23.3270
-- -------------------------------------------------------------

DROP TABLE IF EXISTS "public"."users" CASCADE;
-- This script only contains the table creation statements and does not fully represent the table in the database. It's still missing: indices, triggers. Do not use it as a backup.

-- Sequence and defined type
DROP SEQUENCE IF EXISTS users_id_seq CASCADE;
CREATE SEQUENCE IF NOT EXISTS users_id_seq;

-- Table Definition
CREATE TABLE "public"."users" (
                                  "id" int4 PRIMARY KEY NOT NULL DEFAULT nextval('users_id_seq'::regclass),
                                  "username" varchar NOT NULL UNIQUE,
                                  "email" varchar NOT NULL UNIQUE,
                                  "encrypted_password" varchar NOT NULL,
                                  "profile_picture" varchar
);

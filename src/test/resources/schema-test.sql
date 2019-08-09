DROP TABLE IF EXISTS restaurant CASCADE;
DROP TABLE IF EXISTS dish CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS user_vote CASCADE;

DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE GLOBAL_SEQ START WITH 100000;

CREATE TABLE users
(
  id                BIGINT    DEFAULT global_seq.nextval PRIMARY KEY,
  name              VARCHAR(255)            NOT NULL,
  email             VARCHAR(255)            NOT NULL,
  password          VARCHAR(255)            NOT NULL,
  registration_time TIMESTAMP DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);
CREATE UNIQUE INDEX users_unique_ename_idx ON users (name);

CREATE TABLE restaurant
(
  id      BIGINT DEFAULT global_seq.nextval PRIMARY KEY,
  name    VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  user_id BIGINT       NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE dish
(
  id            BIGINT DEFAULT global_seq.nextval PRIMARY KEY,
  name          VARCHAR(255)                NOT NULL,
  price         NUMERIC(7, 2)               NOT NULL,
  restaurant_id INTEGER                     NOT NULL,
  date          DATE   DEFAULT current_date NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX dish_unique_idx ON dish (restaurant_id, date, name);

CREATE TABLE role
(
  id   BIGINT DEFAULT global_seq.nextval PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX role_unique_name_idx ON role (name);

CREATE TABLE user_roles
(
  user_id  BIGINT NOT NULL,
  roles_id BIGINT NOT NULL,
  CONSTRAINT user_roles_idx UNIQUE (user_id, roles_id),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  FOREIGN KEY (roles_id) REFERENCES role (id) ON DELETE CASCADE
);

CREATE TABLE user_vote
(
  id            BIGINT DEFAULT global_seq.nextval PRIMARY KEY,
  user_id       BIGINT                      NOT NULL,
  restaurant_id BIGINT                      NOT NULL,
  date          DATE   DEFAULT current_date NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uservote_unique_restaurant_user_date_idx ON user_vote (user_id, date);
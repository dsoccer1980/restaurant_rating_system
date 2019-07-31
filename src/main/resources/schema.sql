DROP TABLE IF EXISTS restaurant CASCADE;
DROP TABLE IF EXISTS dish CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS user_vote CASCADE;
DROP SEQUENCE IF EXISTS GLOBAL_SEQ;

CREATE SEQUENCE GLOBAL_SEQ START WITH 100000;

CREATE TABLE restaurant
(
  id      BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name    VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL
);

CREATE TABLE dish
(
  id            BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name          VARCHAR(255)                            NOT NULL,
  price         NUMERIC(5, 2)                           NOT NULL,
  restaurant_id INTEGER                                 NOT NULL,
  date          DATE               DEFAULT current_date NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX dish_unique_idx ON dish (restaurant_id, date, name);

CREATE TABLE users
(
  id         BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  name       VARCHAR(255)                     NOT NULL,
  email      VARCHAR(255)                     NOT NULL,
  password   VARCHAR(255)                     NOT NULL,
  registered TIMESTAMP          DEFAULT now() NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id BIGINT NOT NULL,
  role    VARCHAR(255),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_vote
(
  id            BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
  user_id       BIGINT                                  NOT NULL,
  restaurant_id BIGINT                                  NOT NULL,
  date          DATE               DEFAULT current_date NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT (id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX uservote_unique_restaurant_user_date_idx ON user_vote (user_id, date);
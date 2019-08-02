DELETE FROM restaurant;
DELETE FROM users;
DELETE FROM dish;
DELETE FROM role;
DELETE FROM user_vote;


INSERT INTO users values (1, 'Ivanov', 'ivan@gmail.com', 'password', '2019-07-31');
INSERT INTO users values (2, 'Petrov', 'petr@gmail.com', 'password2', '2019-07-31');

INSERT INTO restaurant(id, name, address, user_id) values (10, 'TSAR', 'Nevskij 53', 1);
INSERT INTO restaurant(id, name, address, user_id) values (11, 'Europe', 'Mihailovskaja 14', 2);

INSERT INTO dish values (20, 'Borsh', 255.3, 10, '2019-07-24');
INSERT INTO dish values (21, 'Soljanka', 235.3, 11, '2019-07-23');

INSERT INTO role values (40, 'COMPANY');
INSERT INTO role values (41, 'USER');

INSERT INTO user_vote values (30, 1, 10, '2019-07-24');
INSERT INTO user_vote values (31, 2, 10, '2019-07-24');
INSERT INTO user_vote values (32, 2, 11, '2019-07-25');



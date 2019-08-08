DELETE
FROM restaurant;
DELETE
FROM users;
DELETE
FROM dish;
DELETE
FROM role;
DELETE
FROM user_roles;
DELETE
FROM user_vote;


INSERT INTO users
values (1, 'user', 'user@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users
values (2, 'company', 'company@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.',
        '2019-07-31');

INSERT INTO restaurant(id, name, address, user_id)
values (10, 'TSAR', 'Nevskij 53', 1);
INSERT INTO restaurant(id, name, address, user_id)
values (11, 'Europe', 'Mihailovskaja 14', 2);

INSERT INTO dish
values (20, 'Borsh', 255.3, 10, '2019-07-24');
INSERT INTO dish
values (21, 'Soljanka', 235.3, 11, '2019-07-23');

INSERT INTO role
values (40, 'USER');
INSERT INTO role
values (41, 'COMPANY');

INSERT INTO user_roles
VALUES (1, 40);
INSERT INTO user_roles
VALUES (2, 41);

INSERT INTO user_vote
values (30, 1, 10, '2019-07-24');
INSERT INTO user_vote
values (31, 2, 10, '2019-07-24');
INSERT INTO user_vote
values (32, 2, 11, '2019-07-25');
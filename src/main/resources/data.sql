DELETE FROM restaurant;
DELETE FROM users;
DELETE FROM dish;
DELETE FROM role;
DELETE FROM user_roles;
DELETE FROM user_vote;


INSERT INTO users values (1, 'user', 'user@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (2, 'company', 'company@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (3, 'company2', 'company2@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');

INSERT INTO restaurant(id, name, address, user_id) values (10, 'Астория', 'Большая Морская ул., 39', 2);
INSERT INTO restaurant(id, name, address, user_id) values (11, 'Метрополь', 'Садовая ул., 22/2', 3);

INSERT INTO dish values (20, 'Солянка', 700, 10, '2019-07-24');
INSERT INTO dish values (21, 'Жареное каре ягненка', 980, 10, '2019-07-24');
INSERT INTO dish values (22, 'Мясной бульон', 400, 10, NOW());
INSERT INTO dish values (23, 'Филе телятины на гриле', 990, 10, NOW());
INSERT INTO dish values (24, 'Медовик ', 450, 10, NOW());
INSERT INTO dish values (25, 'Уха «Царская»', 790, 11, NOW());
INSERT INTO dish values (26, 'Котлета Фаберже', 720, 11, NOW());
INSERT INTO dish values (27, 'Шоколадный фондан ', 450, 11, NOW());

INSERT INTO role values (40, 'USER');
INSERT INTO role values (41, 'COMPANY');

INSERT INTO user_roles VALUES (1, 40);
INSERT INTO user_roles VALUES (2, 41);
INSERT INTO user_roles VALUES (3, 41);

INSERT INTO user_vote values (30, 1, 10, '2019-07-24');
INSERT INTO user_vote values (31, 2, 10, '2019-07-24');
INSERT INTO user_vote values (32, 2, 11, '2019-07-25');
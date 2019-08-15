DELETE FROM restaurant;
DELETE FROM users;
DELETE FROM dish;
DELETE FROM role;
DELETE FROM user_roles;
DELETE FROM user_vote;


INSERT INTO users values (1, 'user', 'user@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (4, 'user2', 'user2@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (5, 'user3', 'user3@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (2, 'company', 'company@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (3, 'company2', 'company2@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-31');
INSERT INTO users values (7, 'company3', 'company3@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-30');
INSERT INTO users values (6, 'admin', 'admin@gmail.com', '$2a$10$9vBkM90.Nc5WfI9MmGmCv.h2zxTn/imIq2n1HD/9w8/CNMRrn3oN.', '2019-07-30');

INSERT INTO restaurant(id, name, address, user_id) values (10, 'Астория', 'Большая Морская ул., 39', 2);
INSERT INTO restaurant(id, name, address, user_id) values (11, 'Метрополь', 'Садовая ул., 22/2', 3);
INSERT INTO restaurant(id, name, address, user_id) values (12, 'Флорентини', 'Проспект Вернадского, 15', 7);

INSERT INTO dish values (20, 'Солянка', 700, 10, '2019-07-24');
INSERT INTO dish values (21, 'Жареное каре ягненка', 980, 10, '2019-07-24');
INSERT INTO dish values (61, 'Овощной салат с креветками', 690, 10, '2019-07-28');
INSERT INTO dish values (62, 'Окрошка на квасе', 450, 10, '2019-07-28');
INSERT INTO dish values (63, 'Бифштекс ребленый', 1090, 10, '2019-07-28');



INSERT INTO dish values (22, 'Мясной бульон', 400, 10, NOW());
INSERT INTO dish values (23, 'Филе телятины на гриле', 990, 10, NOW());
INSERT INTO dish values (24, 'Медовик ', 450, 10, NOW());
INSERT INTO dish values (25, 'Уха «Царская»', 790, 11, NOW());
INSERT INTO dish values (26, 'Котлета Фаберже', 720, 11, NOW());
INSERT INTO dish values (27, 'Шоколадный фондан', 450, 11, NOW());
INSERT INTO dish values (28, 'Котлетки из краба с пюре', 990, 12, NOW());
INSERT INTO dish values (29, 'Салат из печёного баклажана', 540, 12, NOW());
INSERT INTO dish values (30, 'Утиная грудка с яблочным штруделем', 720, 12, NOW());

INSERT INTO role values (40, 'USER');
INSERT INTO role values (41, 'COMPANY');
INSERT INTO role values (42, 'ADMIN');

INSERT INTO user_roles VALUES (1, 40);
INSERT INTO user_roles VALUES (4, 40);
INSERT INTO user_roles VALUES (5, 40);
INSERT INTO user_roles VALUES (2, 41);
INSERT INTO user_roles VALUES (3, 41);
INSERT INTO user_roles VALUES (6, 42);
INSERT INTO user_roles VALUES (7, 41);

INSERT INTO user_vote values (30, 1, 11, '2019-07-24');
INSERT INTO user_vote values (34, 1, 10, '2019-07-25');
INSERT INTO user_vote values (35, 1, 11, '2019-07-27');
INSERT INTO user_vote values (36, 1, 12, '2019-07-28');
INSERT INTO user_vote values (31, 4, 10, '2019-07-24');
INSERT INTO user_vote values (32, 5, 10, '2019-07-24');
INSERT INTO user_vote values (33, 4, 11, '2019-07-25');
INSERT INTO user_vote values (38, 4, 12, '2019-07-27');
INSERT INTO user_vote values (39, 4, 12, '2019-07-28');
INSERT INTO user_vote values (51, 5, 11, '2019-07-28');
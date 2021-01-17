DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO USERS (name, email, password)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('User1', 'user1@yandex.ru', '{noop}password'),
       ('User2', 'user2@yandex.ru', '{noop}wordpass');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('ADMIN', 100),
       ('USER', 101),
       ('USER', 102);

INSERT INTO RESTAURANTS (description)
VALUES ('Метрополь'),
       ('Пронтера'),
       ('Папа Джонс');

INSERT INTO DISHES (day_value, description, price, rest_id)
VALUES ('2020-12-30', 'Завтрак', 500, 103),
       ('2020-12-30', 'Обед', 1000, 104),
       ('2020-12-30', 'Ужин', 500, 105),
       ('2020-12-31', 'Завтрак', 500, 103),
       ('2020-12-31', 'Обед', 1000, 104),
       ('2020-12-31', 'Ужин', 510, 105);

INSERT INTO VOTES(user_id, vote_date, rest_id)
VALUES (101, '2020-12-30', 105),
       (102, '2020-12-30', 103),
       (101, '2020-12-31', 104),
       (102, '2020-12-31', 105);
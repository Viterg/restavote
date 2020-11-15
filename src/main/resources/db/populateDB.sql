DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, email, password)
VALUES ('Admin', 'admin@gmail.com', 'admin'),
       ('User', 'user@yandex.ru', 'password');

INSERT INTO user_roles (role, user_id)
VALUES ('ADMIN', 100),
       ('USER', 101);

INSERT INTO RESTAURANTS (description)
VALUES ('Метрополь'),
       ('Пронтера'),
       ('Папа Джонс');

INSERT INTO dishes (date_time, description, price, rest_id)
VALUES ('2020-01-30 10:00:00', 'Завтрак', 500, 102),
       ('2020-01-30 13:00:00', 'Обед', 1000, 103),
       ('2020-01-30 20:00:00', 'Ужин', 500, 104),
       ('2020-01-31 10:00:00', 'Завтрак', 500, 102),
       ('2020-01-31 13:00:00', 'Обед', 1000, 103),
       ('2020-01-31 20:00:00', 'Ужин', 510, 104);
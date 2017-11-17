DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
  (100000, '2017-11-10T10:00', 'Завтрак', 500),
  (100000, '2017-11-10T14:00', 'Обед', 1000),
  (100000, '2017-11-10T19:00', 'Ужин', 500),
  (100000, '2017-11-11T10:00', 'Завтрак', 500),
  (100000, '2017-11-11T14:00', 'Обед', 1000),
  (100000, '2017-11-11T19:00', 'Ужин', 550);
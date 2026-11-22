SET client_encoding = 'UTF8';

DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
    id         SERIAL PRIMARY KEY,
    full_name  VARCHAR(100) NOT NULL,
    country    VARCHAR(50),
    birth_year INTEGER
);

CREATE TABLE book (
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(200) NOT NULL,
    genre        VARCHAR(50),
    publish_year INTEGER,
    pages        INTEGER,
    author_id    INTEGER NOT NULL REFERENCES author (id) ON DELETE CASCADE
);

INSERT INTO author (full_name, country, birth_year) VALUES
    ('Лев Толстой', 'Россия', 1828),
    ('Фёдор Достоевский', 'Россия', 1821),
    ('Джордж Оруэлл', 'Великобритания', 1903),
    ('Рэй Брэдбери', 'США', 1920);

INSERT INTO book (title, genre, publish_year, pages, author_id) VALUES
    ('Война и мир', 'Роман', 1869, 1300, 1),
    ('Анна Каренина', 'Роман', 1877, 860, 1),
    ('Преступление и наказание', 'Роман', 1866, 670, 2),
    ('Идиот', 'Роман', 1869, 640, 2),
    ('1984', 'Антиутопия', 1949, 320, 3),
    ('Скотный двор', 'Повесть', 1945, 110, 3),
    ('451 градус по Фаренгейту', 'Антиутопия', 1953, 250, 4);

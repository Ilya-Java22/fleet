create table if not exists users
(
    id serial primary key,
    username varchar NOT NULL unique,
    password varchar NOT NULL
);
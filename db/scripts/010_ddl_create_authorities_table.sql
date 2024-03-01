create table if not exists authorities
(
    id serial primary key,
    name varchar NOT NULL unique
);
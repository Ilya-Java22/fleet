create table if not exists authorities
(
    id serial primary key,
    authority varchar NOT NULL unique
);
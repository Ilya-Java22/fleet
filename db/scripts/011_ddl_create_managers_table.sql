create table if not exists managers
(
    id serial primary key,
    username varchar NOT NULL unique,
    password varchar NOT NULL,
    authority_id int not null references authorities(id)
);
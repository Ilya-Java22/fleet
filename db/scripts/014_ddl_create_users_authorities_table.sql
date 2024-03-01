create table if not exists users_authorities
(
    id serial primary key,
    user_id int references users(id),
    authority_id int references authorities(id)
);
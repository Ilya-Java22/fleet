create table if not exists users_enterprises
(
    id serial primary key,
    user_id int references users(id),
    enterprise_id int references enterprises(id)
);
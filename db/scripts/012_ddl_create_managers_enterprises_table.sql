create table if not exists managers_enterprises
(
    id serial primary key,
    manager_id int references managers(id),
    enterprise_id int references enterprises(id)
);
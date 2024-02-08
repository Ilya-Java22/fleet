create table if not exists drivers
(
    id   serial primary key,
    name varchar,
    salary numeric,
    enterprise_id int REFERENCES enterprises(id)
);
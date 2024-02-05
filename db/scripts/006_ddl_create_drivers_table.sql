create table if not exists drivers
(
    id   serial primary key,
    name int,
    salary numeric,
    enterprise_id int REFERENCES enterprises(id)
);
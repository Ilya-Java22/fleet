create table brands
(
    id   serial primary key,
    brand varchar,
    type varchar,
    tank_capacity int,
    lifting_capacity int,
    capacity int
);
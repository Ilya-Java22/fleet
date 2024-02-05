create table if not exists vehicles
(
    id   serial primary key,
    release_year int,
    mileage int,
    price numeric
);
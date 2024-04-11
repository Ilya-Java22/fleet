create table if not exists trips
(
    id serial primary key,
    vehicle_id int references vehicles(id) NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE,
    finish_time TIMESTAMP WITHOUT TIME ZONE
    );

CREATE INDEX vehicle_start_time ON trips (vehicle_id, start_time);
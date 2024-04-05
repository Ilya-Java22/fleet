create table if not exists vehicles_track_points
(
    id serial primary key,
    vehicle_id int references vehicles(id) NOT NULL,
    time TIMESTAMP WITHOUT TIME ZONE,
    point GEOMETRY(Point,4326)
);

CREATE INDEX vehicle_time ON vehicles_track_points (vehicle_id, time);
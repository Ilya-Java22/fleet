create table if not exists active_drivers_vehicles
(
    id serial primary key,
    vehicle_id int unique references vehicles(id),
    driver_id int unique references drivers(id)
);
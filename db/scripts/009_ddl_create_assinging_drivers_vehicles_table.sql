create table if not exists assinging_drivers_vehicles
(
    id serial primary key,
    vehicle_id int references vehicles(id) ,
    driver_id int references drivers(id)
);
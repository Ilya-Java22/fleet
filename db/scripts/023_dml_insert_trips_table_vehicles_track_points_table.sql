insert into trips (vehicle_id, start_time, finish_time)
values (1, '2024-02-28 13:29:00', '2024-02-28 13:55:00');

insert into trips (vehicle_id, start_time, finish_time)
values (1, '2024-02-28 01:00:00', '2024-02-28 01:25:00');

UPDATE vehicles_track_points
SET trip_id =
    CASE 
        WHEN id between 1 and 5 THEN (select id from trips where start_time = '2024-02-28 13:29:00' LIMIT 1)
        ELSE (select id from trips where start_time = '2024-02-28 01:00:00' LIMIT 1)
    END;
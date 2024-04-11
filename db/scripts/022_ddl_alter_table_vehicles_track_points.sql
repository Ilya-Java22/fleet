ALTER TABLE vehicles_track_points DROP COLUMN vehicle_id;
--DROP INDEX vehicle_time;
ALTER TABLE vehicles_track_points ADD COLUMN trip_id int references trips(id);
CREATE INDEX trip_index ON vehicles_track_points (trip_id);

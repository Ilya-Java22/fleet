package ru.skillsmart.fleet.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TrackPointRepository extends JpaRepository<TrackPoint, Integer> {
    @Query("SELECT tp FROM TrackPoint tp WHERE tp.trip.id IN :tripIds ORDER BY tp.trip.id, tp.time ASC")
    List<TrackPoint> findPointsSortedByTripIdAndTime(@Param("tripIds") List<Integer> tripIds);
}

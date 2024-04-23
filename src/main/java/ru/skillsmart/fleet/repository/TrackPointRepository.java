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
    //не делает join и доп запросы
    @Query("SELECT tp FROM TrackPoint tp WHERE tp.trip.id IN :tripIds ORDER BY tp.trip.id, tp.time ASC")
    List<TrackPoint> findPointsSortedByTripIdAndTime(@Param("tripIds") List<Integer> tripIds);

    //делает join c trip
//    @Query("SELECT tp FROM TrackPoint tp WHERE tp.trip.vehicle.id = :vehicleId AND tp.time BETWEEN :startDate AND :endDate")
//    List<TrackPoint> findPointsByVehicleIdAndTimeBetween(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}

package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    //JPQL
//    @Query("SELECT tp FROM Trip t JOIN t.trackPoints tp " +
//            "WHERE t.vehicle.id = :vehicleId " +
//            "AND t.startTime >= :startDate AND t.finishTime <= :endDate")
    //HQL
    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.trackPoints tp "
            + "WHERE t.vehicle.id = :vehicleId "
            + "AND t.startTime >= :startDate AND t.finishTime <= :endDate")
    List<Trip> findByVehicleIdAndTimeBetween(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}

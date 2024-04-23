package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.skillsmart.fleet.model.Trip;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    //JPQL
//    @Query("SELECT tp FROM Trip t JOIN t.trackPoints tp " +
//            "WHERE t.vehicle.id = :vehicleId " +
//            "AND t.startTime >= :startDate AND t.finishTime <= :endDate")
    //HQL
    //выдает поездки, которые только полностью попали в диапазон времени
    @Query("SELECT DISTINCT t FROM Trip t LEFT JOIN FETCH t.trackPoints tp "
            + "WHERE t.vehicle.id = :vehicleId "
            + "AND t.startTime >= :startDate AND t.finishTime <= :endDate")
    List<Trip> findTripsWithTrackpointsByVehicleIdAndTimeBetween(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    //выдает поездки, которые только полностью попали в диапазон времени
    @Query("SELECT DISTINCT t FROM Trip t "
            + "WHERE t.vehicle.id = :vehicleId "
            + "AND t.startTime >= :startDate AND t.finishTime <= :endDate")
    List<Trip> findTripsByVehicleIdAndTimeBetween(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);

    //выдает поездки, в том числе которые частично попали в диапазон времени
    @Query("SELECT DISTINCT t FROM Trip t "
            + "WHERE t.vehicle.id = :vehicleId "
            + "AND t.finishTime > :startDate AND t.startTime < :endDate")
    List<Trip> findAllTripsByVehicleIdAndTimeBetween(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}

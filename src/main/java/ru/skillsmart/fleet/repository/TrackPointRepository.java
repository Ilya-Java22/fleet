package ru.skillsmart.fleet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackPointRepository extends JpaRepository<TrackPoint, Integer> {

    @Query(value = "SELECT * FROM vehicles_track_points WHERE vehicle_id = :vehicleId AND time BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<TrackPoint> findByVehicleIdAndTimeBetween(Integer vehicleId, LocalDateTime startDate, LocalDateTime endDate);
}

package ru.skillsmart.fleet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {

    @Query("from Vehicle v JOIN FETCH v.brand")
    List<Vehicle> findAll();

    //@EntityGraph(attributePaths = {"enterprise", "activeDriver", "brand"})
    Optional<Vehicle> findById(int id);

        //не нужен, излишне
//    @EntityGraph(attributePaths = {"assingingDriversToVehicles", "activeDriver"})
//    Optional<Vehicle> findVehicleById(int id);

    //enterprise потому что надо вытаскивать их таймзону при преобразованиях времени для выдачи по ресту
    @Query(value = "from Vehicle v JOIN FETCH v.brand JOIN FETCH v.enterprise",
            countQuery = "select count(v) from Vehicle v")
    Page<Vehicle> findAll(Pageable pageable);

//    @Modifying
//    @Query("delete from Vehicle v where v.id = ?1")
//    int deletebyId(Integer id);


    @Query(nativeQuery = true, value = "select a.vehicle_id from active_drivers_vehicles a where a.driver_id=:activeDriverId AND a.vehicle_id != :vehicleId")
    Integer findByActiveDriverId(int activeDriverId, int vehicleId);

    @Modifying
    @Query("DELETE FROM Vehicle v WHERE v = :vehicle")
    int deleteByVehicle(Vehicle vehicle);
}

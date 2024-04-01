package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    List<Driver> findAll();

    List<Driver> findAllByEnterpriseId(int enterpriseId);

    @Query("select a.driver from AssingingDriversToVehicles a where a.vehicle.id = ?1")
    List<Driver> findAllByVehicleId(int id);

}

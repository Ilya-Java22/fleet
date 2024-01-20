package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
    @Modifying
    @Query("from Vehicle v JOIN FETCH v.brand")
    List<Vehicle> findAll();
}

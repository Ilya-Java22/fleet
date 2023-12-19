package ru.skillsmart.fleet.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
    List<Vehicle> findAll();

}

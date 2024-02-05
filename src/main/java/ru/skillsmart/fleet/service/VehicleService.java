package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<VehicleDTO> findAllDto();
    List<Vehicle> findAll();
    Optional<Vehicle> findById(int id);
    Optional<Vehicle> save(Vehicle vehicle);
    boolean deleteById(int id);
    boolean update(Vehicle vehicle);
}

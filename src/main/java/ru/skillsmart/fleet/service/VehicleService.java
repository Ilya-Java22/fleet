package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.util.Collection;
import java.util.List;

public interface VehicleService {
    List<Vehicle> findAll();
}

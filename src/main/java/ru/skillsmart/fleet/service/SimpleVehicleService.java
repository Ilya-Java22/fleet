package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.util.List;

@Service
public class SimpleVehicleService implements VehicleService {
    private final VehicleRepository vehicleRepository;

    public SimpleVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}

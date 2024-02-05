package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.mapper.VehicleMapper;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleVehicleService implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public SimpleVehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public List<VehicleDTO> findAllDto() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::getModelFromEntity)
                .toList();
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Optional<Vehicle> save(Vehicle vehicle) {
        Vehicle resultVehicle = vehicleRepository.save(vehicle);
        return resultVehicle.getId() != 0 ? Optional.of(resultVehicle) : Optional.empty();
    }

    @Override
    public boolean deleteById(int id) {
        return vehicleRepository.deletebyId(id) > 0;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        return vehicleRepository.findById(vehicle.getId())
                .map(x -> {
                    vehicleRepository.save(vehicle);
                    return true;
                })
                .orElse(false);
    }
}

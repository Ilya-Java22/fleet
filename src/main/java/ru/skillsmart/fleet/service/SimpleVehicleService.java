package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.dto.VehicleCreateDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.mapper.VehicleMapper;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.EnterpriseRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.util.*;

@Service
public class SimpleVehicleService implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserService userService;
    private final EnterpriseRepository enterpriseRepository;
    private final VehicleMapper vehicleMapper;

    public SimpleVehicleService(VehicleRepository vehicleRepository, UserService userService,
                                EnterpriseRepository enterpriseRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
        this.enterpriseRepository = enterpriseRepository;
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
    public Optional<VehicleDTO> saveRest(VehicleCreateDTO vehicleCreateDTO) {
        Vehicle vehicle = vehicleMapper.getEntityFromModel(vehicleCreateDTO);
        vehicleRepository.save(vehicle);
        return vehicle.getId() != 0 ? Optional.of(vehicleMapper.getModelFromEntity(vehicle)) : Optional.empty();
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

    @Override
    public void moveVehicleToEnterprise(Vehicle vehicle, Integer newEnterpriseId) {
//        Vehicle vehicle = vehicleRepository.findById(vehicleId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Vehicle is not found. Please, check id."
//                ));
        vehicle.setEnterprise(enterpriseRepository.getReferenceById(newEnterpriseId));
        vehicleRepository.save(vehicle);
    }

}

package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.EnterpriseRepository;

import java.util.List;
import java.util.Optional;

public interface EnterpriseService {
    List<EnterpriseDTO> findAllDto();
    List<Enterprise> findAllWithVehiclesAndDrivers();
    Optional<Enterprise> findById(int id);
    Optional<Enterprise> save(Enterprise enterprise);
//    boolean deleteById(int id);
    boolean update(Enterprise enterprise);
}

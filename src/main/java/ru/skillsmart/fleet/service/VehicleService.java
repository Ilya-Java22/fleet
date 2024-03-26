package ru.skillsmart.fleet.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillsmart.fleet.dto.VehicleCreateDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    //    заменил на пагинацию, см. ниже
    //    List<VehicleDTO> findAllDto();

    Page<VehicleDTO> findAllDto(Pageable pageable);
    List<Vehicle> findAll();
    Optional<Vehicle> findById(int id);
    Optional<VehicleDTO> findVehicleDTOById(int id);
    boolean deleteByVehicle(Vehicle vehicle);
//    Optional<Vehicle> findVehicleById(int id);

    //используется в SimpleDataGenerationService (не использую там dto)
    Optional<Vehicle> save(Vehicle vehicle);
    Optional<VehicleDTO> saveDTO(VehicleCreateDTO vehicleCreateDTO);
//    boolean deleteById(int id);
    Integer update(VehicleDTO vehicleDTO, int driversCount);
    void moveVehicleToEnterprise(Vehicle vehicle, Integer newEnterpriseId);
}

package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Driver;

import java.util.List;
import java.util.Optional;

public interface DriverService {
    List<Driver> saveAll(List<Driver> driverList);

    List<Driver> findAllByEnterpriseId(int enterpriseId);
    List<Driver> findAllByVehicle(VehicleDTO vehicleDTO);
}

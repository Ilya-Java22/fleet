package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.mapper.VehicleMapper;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.repository.DriverRepository;

import java.util.List;

@Service
public class SimpleDriverService implements DriverService {
    private final DriverRepository driverRepository;
    private final VehicleMapper vehicleMapper;

    public SimpleDriverService(DriverRepository driverRepository, VehicleMapper vehicleMapper) {
        this.driverRepository = driverRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public List<Driver> saveAll(List<Driver> driverList) {
        return driverRepository.saveAll(driverList);
    }

    @Override
    public List<Driver> findAllByEnterpriseId(int enterpriseId) {
        return driverRepository.findAllByEnterpriseId(enterpriseId);
    }

    @Override
    public List<Driver> findAllByVehicleId(int id) {
        return driverRepository.findAllByVehicleId(id);
    }
}

package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.repository.BrandRepository;
import ru.skillsmart.fleet.repository.DriverRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleDriverService implements DriverService {
    private final DriverRepository driverRepository;

    public SimpleDriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> saveAll(List<Driver> driverList) {
        return driverRepository.saveAll(driverList);
    };

}

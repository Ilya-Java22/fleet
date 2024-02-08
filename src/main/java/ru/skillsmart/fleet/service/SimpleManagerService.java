package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.mapper.EnterpriseMapper;
import ru.skillsmart.fleet.mapper.VehicleMapper;
import ru.skillsmart.fleet.model.Manager;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.ManagerRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleManagerService implements ManagerService {

    private final ManagerRepository managerRepository;
    private final EnterpriseMapper enterpriseMapper;

    public SimpleManagerService(ManagerRepository managerRepository, EnterpriseMapper enterpriseMapper) {
        this.managerRepository = managerRepository;
        this.enterpriseMapper = enterpriseMapper;
    }

    @Override
    public Manager findManagerByName(String username) {
        return managerRepository.findManagerByUsername(username);
    }
}

package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Manager;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface ManagerService {
    Manager findManagerByName(String username);
}

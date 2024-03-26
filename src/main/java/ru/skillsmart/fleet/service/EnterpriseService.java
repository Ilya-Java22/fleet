package ru.skillsmart.fleet.service;

import net.bytebuddy.asm.Advice;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.EnterpriseRepository;

import java.util.List;
import java.util.Optional;

public interface EnterpriseService {
    List<Enterprise> findUserEnterprises(String username);
    List<EnterpriseDTO> findAllDto();
    List<EnterpriseDTO> findUserEnterprisesWithDriversVehiclesDto(String username);
    List<Enterprise> findUserEnterprisesWithDriversVehicles(String username);
    Optional<EnterpriseDTO> findById(int id);
    Optional<Enterprise> save(Enterprise enterprise);
    boolean update(Enterprise enterprise);
    boolean checkUserAccessToEnterprise(String username, Integer... enterpriseIds);
    List<Enterprise> findAllById(String[] ids);
    Enterprise getReferenceById(int id);

}

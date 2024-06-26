package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.mapper.EnterpriseMapper;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.repository.EnterpriseRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SimpleEnterpriseService implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;
    private final UserService userService;

    private final EnterpriseMapper enterpriseMapper;

    public SimpleEnterpriseService(EnterpriseRepository enterpriseRepository, EnterpriseMapper enterpriseMapper, UserService userService) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseMapper = enterpriseMapper;
        this.userService = userService;
    }

    @Override
    public List<Enterprise> findUserEnterprises(String username) {
        Set<Enterprise> enterprises = userService.findUserWithEnterprisesByUsername(username).getEnterprises();
        return new ArrayList<>(enterprises);
    }

    @Override
    public List<EnterpriseDTO> findAllDto() {
        return findAllWithVehiclesAndDrivers().stream()
                .map(enterpriseMapper::getModelFromEntity)
                .toList();
    }

    @Override
    public List<EnterpriseDTO> findUserEnterprisesWithDriversVehiclesDto(String username) {
        List<Enterprise> enterprises = findUserEnterprisesWithDriversVehicles(username);
        return enterprises.stream()
                .map(enterpriseMapper::getModelFromEntity)
                .toList();
    }

    @Override
    public List<Enterprise> findUserEnterprisesWithDriversVehicles(String username) {
        return new ArrayList<>(userService.findUserWithEnterprisesWithDriversVehiclesByUsername(username).getEnterprises());
    }


    @Transactional(readOnly = true)
    List<Enterprise> findAllWithVehiclesAndDrivers() {
        List<Enterprise> enterprisesWithVehicles = enterpriseRepository.findAllWithVehicles();
        return !enterprisesWithVehicles.isEmpty() ? enterpriseRepository.findAllWithDrivers() : enterprisesWithVehicles;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EnterpriseDTO> findById(int id) {
        Optional<Enterprise> enterpriseWithVehicles = enterpriseRepository.findByIdWithVehicles(id);
        return enterpriseWithVehicles.isPresent()
                ? enterpriseRepository.findByIdWithDrivers(id)
                .map(enterpriseMapper::getModelFromEntity) : Optional.empty();
    }

    @Override
    public Optional<Enterprise> save(Enterprise enterprise) {
        Enterprise resultEnterprise = enterpriseRepository.save(enterprise);
        return resultEnterprise.getId() != 0 ? Optional.of(resultEnterprise) : Optional.empty();
    }

    @Override
    public boolean update(Enterprise enterprise) {
        return enterpriseRepository.findById(enterprise.getId())
                .map(x -> {
                    enterpriseRepository.save(enterprise);
                    return true;
                })
                .orElse(false);
    }

    public boolean checkUserAccessToEnterprise(String username, Integer... enterpriseIds) {
        Set<Enterprise> userEnterprisesSet = userService.findUserWithEnterprisesByUsername(username).getEnterprises();
        Set<Integer> enterpriseIdSet = new HashSet<>(Arrays.asList(enterpriseIds));
//        return userEnterprisesSet.stream()
//                .map(Enterprise::getId)
//                .anyMatch(enterpriseIdSet::contains);
//        было как выше, но теперь думаю, надо так
        Set<Integer> userEnterprisesIdSet = userEnterprisesSet.stream()
                .map(Enterprise::getId)
                .collect(Collectors.toSet());
        return userEnterprisesIdSet.containsAll(enterpriseIdSet);
    }

    @Override
    public List<Enterprise> findAllById(String[] ids) {
        List<Integer> enterpriseIds = Arrays.stream(ids)
                .map(Integer::valueOf)
                .distinct().toList();
        return enterpriseRepository.findAllById(enterpriseIds);
    }

    @Override
    public Enterprise getReferenceById(int id) {
        return enterpriseRepository.getReferenceById(id);
    }
}

package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.mapper.EnterpriseMapper;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.repository.EnterpriseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleEnterpriseService implements EnterpriseService {

    private final EnterpriseRepository enterpriseRepository;

    private final EnterpriseMapper enterpriseMapper;

    public SimpleEnterpriseService(EnterpriseRepository enterpriseRepository, EnterpriseMapper enterpriseMapper) {
        this.enterpriseRepository = enterpriseRepository;
        this.enterpriseMapper = enterpriseMapper;
    }

    @Override
    public List<EnterpriseDTO> findAllDto() {
        return findAllWithVehiclesAndDrivers().stream()
                .map(enterpriseMapper::getModelFromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Enterprise> findAllWithVehiclesAndDrivers() {
        List<Enterprise> enterprisesWithVehicles = enterpriseRepository.findAllWithVehicles();
        return !enterprisesWithVehicles.isEmpty() ? enterpriseRepository.findAllWithDrivers() : enterprisesWithVehicles;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Enterprise> findById(int id) {
        Optional<Enterprise> enterpriseWithVehicles = enterpriseRepository.findByIdWithVehicles(id);
        return enterpriseWithVehicles.isPresent() ? enterpriseRepository.findByIdWithDrivers(id) : Optional.empty();
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
}

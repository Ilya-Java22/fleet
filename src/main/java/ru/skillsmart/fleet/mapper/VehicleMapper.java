package ru.skillsmart.fleet.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillsmart.fleet.dto.VehicleCreateDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.BrandRepository;
import ru.skillsmart.fleet.repository.EnterpriseRepository;

@Mapper(componentModel = "spring", uses = {EnterpriseRepository.class, BrandRepository.class})
public abstract class VehicleMapper {

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    BrandRepository brandRepository;

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "enterpriseId", source = "enterprise.id")
    @Mapping(target = "activeDriverId", source = "activeDriver.id")
    public abstract VehicleDTO getModelFromEntity(Vehicle vehicle);

    @Mapping(target = "enterprise", expression = "java(enterpriseRepository.getReferenceById(vehicleCreateDTO.getEnterpriseId()))")
    @Mapping(target = "brand", expression = "java(brandRepository.getReferenceById(vehicleCreateDTO.getBrandId()))")
    public abstract Vehicle getEntityFromModel(VehicleCreateDTO vehicleCreateDTO);
}

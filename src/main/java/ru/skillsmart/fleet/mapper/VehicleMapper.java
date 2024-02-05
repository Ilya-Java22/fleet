package ru.skillsmart.fleet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "enterpriseId", source = "enterprise.id")
    @Mapping(target = "activeDriverId", source = "activeDriver.id")
    VehicleDTO getModelFromEntity(Vehicle vehicle);
}

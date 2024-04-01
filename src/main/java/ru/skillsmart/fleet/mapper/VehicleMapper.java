package ru.skillsmart.fleet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillsmart.fleet.dto.VehicleCreateDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.dto.VehicleDTOWithZonedDateTime;
import ru.skillsmart.fleet.dto.VehicleUpdateDTO;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.BrandRepository;
import ru.skillsmart.fleet.repository.DriverRepository;
import ru.skillsmart.fleet.repository.EnterpriseRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(componentModel = "spring", uses = {EnterpriseRepository.class, BrandRepository.class})
public abstract class VehicleMapper {

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    DriverRepository driverRepository;

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "enterpriseId", source = "enterprise.id")
    @Mapping(target = "activeDriverId", source = "activeDriver.id")
    public abstract VehicleDTO getModelFromEntity(Vehicle vehicle);

    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "enterpriseId", source = "enterprise.id")
    @Mapping(target = "enterpriseTimeZone", source = "enterprise.timezone")
    @Mapping(target = "activeDriverId", source = "activeDriver.id")
    @Mapping(target = "purchased", expression = "java(getDefaultZonedDateTime(vehicle.getPurchased()))")
    public abstract VehicleDTOWithZonedDateTime getModelWithDefaultZonedDateTimeFromEntity(Vehicle vehicle);

//    @Mapping(target = "enterprise", expression = "java(enterpriseRepository.getReferenceById(vehicleDTO.getEnterpriseId()))")
//    @Mapping(target = "brand", expression = "java(brandRepository.getReferenceById(vehicleDTO.getBrandId()))")
//    @Mapping(target = "activeDriver", expression = "java(driverRepository.getReferenceById(vehicleDTO.getActiveDriverId()))")
//    public abstract Vehicle getEntityFromDTOModel(VehicleDTO vehicleDTO);

    @Mapping(target = "enterprise", expression = "java(enterpriseRepository.getReferenceById(vehicleCreateDTO.getEnterpriseId()))")
    @Mapping(target = "brand", expression = "java(brandRepository.getReferenceById(vehicleCreateDTO.getBrandId()))")
    public abstract Vehicle getEntityFromCreateDTOModel(VehicleCreateDTO vehicleCreateDTO);

    @Mapping(target = "enterprise", expression = "java(enterpriseRepository.getReferenceById(vehicleUpdateDTO.getEnterpriseId()))")
    @Mapping(target = "brand", expression = "java(brandRepository.getReferenceById(vehicleUpdateDTO.getBrandId()))")
    @Mapping(target = "activeDriver", expression = "java(getActiveDriver(vehicleUpdateDTO.getActiveDriverId()))")
    public abstract void update(VehicleUpdateDTO vehicleUpdateDTO, @MappingTarget Vehicle vehicle);

    Driver getActiveDriver(int activeDriverId) {
        return activeDriverId == 0 ? null : driverRepository.getReferenceById(activeDriverId);
    }

    ZonedDateTime getDefaultZonedDateTime(LocalDateTime localDateTime) {
        return ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
    }
}

package ru.skillsmart.fleet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;

@Mapper(componentModel = "spring")
public interface EnterpriseMapper {
        //при небольшом размере коллекций ок, при больших - скорее всего надо убрать
        @Mapping(target = "driversIdSet", source = "driversSet", qualifiedByName = "driverToId")
        @Mapping(target = "vehiclesIdSet", source = "vehiclesSet", qualifiedByName = "vehicleToId")
        EnterpriseDTO getModelFromEntity(Enterprise enterprise);

        @Named("driverToId")
        static Integer driverToId(Driver driver) {
                return driver.getId();
        }

        @Named("vehicleToId")
        static Integer vehicleToId(Vehicle vehicle) {
                return vehicle.getId();
        }
}

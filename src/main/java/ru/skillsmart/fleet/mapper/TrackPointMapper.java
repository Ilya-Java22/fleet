package ru.skillsmart.fleet.mapper;

import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Vehicle;

@Mapper(componentModel = "spring")
public interface TrackPointMapper {
        @Mapping(target = "coordinateX", source = "point", qualifiedByName = "pointToX")
        @Mapping(target = "coordinateY", source = "point", qualifiedByName = "pointToY")
        //@Mapping(target = "vehicleId", source = "vehicle.id")
        TrackPointDTO getModelFromEntity(TrackPoint trackPoint);

        @Named("pointToX")
        static Double pointToX(Point point) {
                return point.getX();
        }

        @Named("pointToY")
        static Double pointToY(Point point) {
                return point.getY();
        }
}

package ru.skillsmart.fleet.mapper;

import com.graphhopper.api.GraphHopperGeocoding;
import com.graphhopper.api.model.GHGeocodingEntry;
import com.graphhopper.api.model.GHGeocodingRequest;
import com.graphhopper.api.model.GHGeocodingResponse;
import com.graphhopper.util.shapes.GHPoint;
import okhttp3.OkHttpClient;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.dto.TripDTO;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Trip;

import java.util.concurrent.TimeUnit;

@Mapper(componentModel = "spring")
public abstract class TripMapper {

        @Mapping(target = "tripId", source = "id")
        @Mapping(target = "startLat", source = "startPoint", qualifiedByName = "pointToLat")
        @Mapping(target = "startLon", source = "startPoint", qualifiedByName = "pointToLon")
        @Mapping(target = "finishLat", source = "finishPoint", qualifiedByName = "pointToLat")
        @Mapping(target = "finishLon", source = "finishPoint", qualifiedByName = "pointToLon")
        //какой-то тонкий момент, у нас нет нового запроса к базе, даже если мы запросим trip.vehicle.mileage, хотя
        //мы не делали leftjoin, eagle или entitygraph для vehicle. Мы загружали vehicle полностью в начале метода для
        //авторизации, но у нас нет транзакции, кэша 2 уровня итп...
        @Mapping(target = "vehicleId", source = "trip.vehicle.id")
        public abstract TripDTO getModelFromEntity(Trip trip);

        @Named("pointToLat")
        Double pointToLat(Point point) {
                return point.getX();
        }

        @Named("pointToLon")
        Double pointToLon(Point point) {
                return point.getY();
        }

}

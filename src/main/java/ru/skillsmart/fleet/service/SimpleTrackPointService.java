package ru.skillsmart.fleet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.mapper.TrackPointMapper;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.TrackPointRepository;
import ru.skillsmart.fleet.utility.TimeZoneUtility;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SimpleTrackPointService implements TrackPointService {

    private final TrackPointRepository trackPointRepository;
    private final TrackPointMapper trackPointMapper;


    public SimpleTrackPointService(TrackPointRepository trackPointRepository, TrackPointMapper trackPointMapper) {
        this.trackPointRepository = trackPointRepository;
        this.trackPointMapper = trackPointMapper;
    }

    @Override
    public List<TrackPointDTO> getTrackPointsByVehicleAndDateRange(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {
        List<TrackPoint> trackPoints = trackPointRepository.findByVehicleIdAndTimeBetween(vehicle.getId(), startDate, endDate);
        if (trackPoints.isEmpty()) {
            return new ArrayList<>();
        }

        List<TrackPointDTO> trackPointsDTO = trackPoints.stream()
                .map(trackPointMapper::getModelFromEntity)
                .collect(Collectors.toList());

        var defaultZone = TimeZone.getDefault().toZoneId();
        String enterpriseTimeZone = vehicle.getEnterprise().getTimezone();

        trackPointsDTO.forEach(tp -> {
            LocalDateTime timeWithInitialTZ = tp.getTime();
            LocalDateTime timeWithNewTZ = TimeZoneUtility.convertTimeZone(timeWithInitialTZ, defaultZone, enterpriseTimeZone);
            tp.setTime(timeWithNewTZ);
        });

        return trackPointsDTO;
    }

    @Override
    public ObjectNode convertTrackPointsToGeoJson(List<TrackPointDTO> trackPoints) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode geoJson = objectMapper.createObjectNode();
        geoJson.put("type", "FeatureCollection");
        ArrayNode features = geoJson.putArray("features");

        for (TrackPointDTO trackPoint : trackPoints) {
            ObjectNode feature = JsonNodeFactory.instance.objectNode();
            feature.put("type", "Feature");

            ObjectNode properties = feature.putObject("properties");
            properties.put("time", trackPoint.getTime().toString());

            ObjectNode geometry = feature.putObject("geometry");
            geometry.put("type", "Point");

            ArrayNode coordinates = objectMapper.createArrayNode();
//            coordinates.add(trackPoint.getPoint().getX());
//            coordinates.add(trackPoint.getPoint().getY());
            coordinates.add(trackPoint.getCoordinateX());
            coordinates.add(trackPoint.getCoordinateY());
            geometry.set("coordinates", coordinates);

            features.add(feature);
        }

        return geoJson;
    }
    // пишут еще про какой-то классный способ с GeoGSON:
//    Geometry geometry = new GsonBuilder()
//            .registerTypeAdapterFactory(new GeometryAdapterFactory())
//            .create()
//            .fromJson("{\"type\":\"Point\",\"coordinates\": [23.5,20.125]}");
//    https://gis.stackexchange.com/questions/130913/geojson-java-library
}

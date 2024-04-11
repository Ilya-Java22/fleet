package ru.skillsmart.fleet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.mapper.TrackPointMapper;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Trip;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.TripRepository;
import ru.skillsmart.fleet.utility.TimeZoneUtility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class SimpleTripService implements TripService {

    private final TripRepository tripRepository;
    private final TrackPointMapper trackPointMapper;


    public SimpleTripService(TripRepository tripRepository, TrackPointMapper trackPointMapper) {
        this.tripRepository = tripRepository;
        this.trackPointMapper = trackPointMapper;
    }

    @Override
    public List<TrackPointDTO> getTripsPointsByVehicleAndDateRange(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate) {
        List<Trip> trips = tripRepository.findByVehicleIdAndTimeBetween(vehicle.getId(), startDate, endDate);

        if (trips.isEmpty()) {
            return new ArrayList<>();
        }

        List<TrackPoint> orderedTrackPoints = trips.stream().
                flatMap(trip -> trip.getTrackPoints().stream())
                .sorted(Comparator.comparing(TrackPoint::getTime))
                .toList();

        List<TrackPointDTO> trackPointsDTO = orderedTrackPoints.stream()
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
    public ObjectNode convertTripsPointsToGeoJson(List<TrackPointDTO> trackPoints) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode geoJson = objectMapper.createObjectNode();
        geoJson.put("type", "FeatureCollection");
        ArrayNode features = geoJson.putArray("features");

        for (TrackPointDTO trackPoint : trackPoints) {
            ObjectNode feature = JsonNodeFactory.instance.objectNode();
            feature.put("type", "Feature");

            ObjectNode properties = feature.putObject("properties");
            properties.put("time", trackPoint.getTime().toString());
            properties.put("tripId", trackPoint.getTripId());

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

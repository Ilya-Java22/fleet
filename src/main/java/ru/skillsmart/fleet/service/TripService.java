package ru.skillsmart.fleet.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;

public interface TripService {
    List<TrackPointDTO> getTripsPointsByVehicleAndDateRange(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate);

    ObjectNode convertTripsPointsToGeoJson(List<TrackPointDTO> trackPoints);
}

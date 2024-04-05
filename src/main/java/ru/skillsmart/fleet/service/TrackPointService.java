package ru.skillsmart.fleet.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillsmart.fleet.dto.*;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Vehicle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrackPointService {
    List<TrackPointDTO> getTrackPointsByVehicleAndDateRange(Vehicle vehicle, LocalDateTime startDate, LocalDateTime endDate);

    ObjectNode convertTrackPointsToGeoJson(List<TrackPointDTO> trackPoints);
}

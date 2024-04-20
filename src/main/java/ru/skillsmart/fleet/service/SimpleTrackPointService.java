package ru.skillsmart.fleet.service;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.mapper.TrackPointMapper;
import ru.skillsmart.fleet.mapper.TripMapper;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.repository.TrackPointRepository;
import ru.skillsmart.fleet.repository.TripRepository;
import ru.skillsmart.fleet.utility.TimeZoneUtility;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SimpleTrackPointService implements TrackPointService {

    private final TripRepository tripRepository;
    private final TrackPointRepository trackPointRepository;
    private final TrackPointMapper trackPointMapper;
    private final TripMapper tripMapper;


    public SimpleTrackPointService(TripRepository tripRepository, TrackPointRepository trackPointRepository, TrackPointMapper trackPointMapper, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.trackPointRepository = trackPointRepository;
        this.trackPointMapper = trackPointMapper;
        this.tripMapper = tripMapper;
    }

    @Override
    public List<double[][]> getTripsPoints(List<Integer> tripsIds) {

        List<TrackPoint> sortedTrackPoints = trackPointRepository.findPointsSortedByTripIdAndTime(tripsIds);

        if (sortedTrackPoints.isEmpty()) {
            return new ArrayList<>();
        }

        // Группировка точек по ID поездки и создание массивов координат
        Map<Integer, List<Point>> groupedPoints = sortedTrackPoints.stream()
                .collect(Collectors.groupingBy(tp -> tp.getTrip().getId(),
                        Collectors.mapping(TrackPoint::getPoint, Collectors.toList())));

        List<double[][]> result = new ArrayList<>();
        for (List<Point> points : groupedPoints.values()) {
            double[][] coordinatesArray = new double[points.size()][2];
            for (int i = 0; i < points.size(); i++) {
                coordinatesArray[i][0] = points.get(i).getX();
                coordinatesArray[i][1] = points.get(i).getY();
            }
            result.add(coordinatesArray);
        }
        return result;
     }
}

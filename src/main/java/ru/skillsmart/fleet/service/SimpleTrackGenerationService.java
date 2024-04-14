package ru.skillsmart.fleet.service;


import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;
import com.graphhopper.api.GraphHopperGeocoding;
import com.graphhopper.api.GraphHopperWeb;
import com.graphhopper.api.model.GHGeocodingEntry;
import com.graphhopper.api.model.GHGeocodingRequest;
import com.graphhopper.api.model.GHGeocodingResponse;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.GHPoint;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Trip;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.TripRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SimpleTrackGenerationService implements TrackGenerationService {
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final GraphHopperWeb graphHopper;
    private final GraphHopperGeocoding geocoding;

    public SimpleTrackGenerationService(TripRepository tripRepository, VehicleRepository vehicleRepository,
                                        GraphHopperWeb graphhopper, GraphHopperGeocoding geocoding) {
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
        this.graphHopper = graphhopper;
        this.geocoding = geocoding;
    }

    //на широте Москвы, 1 мин шир = 1 км, 1 мин долг = 1.85 км - это в терминах 60 мин = гр,
    //если мы работаем в формате координат 56.999, то 1 мин = 0.63 км
    public static final int METERS_TO_DEGREES = 63000;


    //надо бы разбить на методы))
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateTrack(Integer vehicleId, Double trackLength, String centralPoint, Integer trackStep) {

        PointList pl = getGraphhopperPointList(trackLength, centralPoint);
        if (pl == null) {
            return;
        }

        Object[] resultingPointListAndDistance = getPoints(trackLength, trackStep, pl);
        List<Point> resultingPointList = (List<Point>) resultingPointListAndDistance[0];
        double factDistance = (Math.round((double) resultingPointListAndDistance[1] * 100)) * 1.0 / 100;

        if (!resultingPointList.isEmpty()) {
//                Vehicle vehicle = new Vehicle();
//        		vehicle.setId(vehicleId);

            Vehicle vehicle = vehicleRepository.getReferenceById(vehicleId);
            Trip trip = new Trip();
            trip.setStartTime(LocalDateTime.now().withNano(0));
            trip.setVehicle(vehicle);
            trip.setStartPoint(resultingPointList.get(0));
            trip.setStartAddress(pointToAddress(resultingPointList.get(0)));
            tripRepository.save(trip);

            for (Point point : resultingPointList) {
                TrackPoint trackPoint = new TrackPoint();
                trackPoint.setTime(LocalDateTime.now().withNano(0));
                trackPoint.setPoint(point);
//                        trackPoint.setVehicle(vehicle);
//                        trackPointRepository.save(trackPoint);
                trip.addTrackPoint(trackPoint);
                tripRepository.save(trip);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            trip.setFinishTime(LocalDateTime.now().withNano(0));
            trip.setFinishPoint(resultingPointList.get(resultingPointList.size() - 1));
            trip.setFinishAddress(pointToAddress(resultingPointList.get(resultingPointList.size() - 1)));
            trip.setDistanceInMeters(factDistance);
            tripRepository.save(trip);
        }
    }

    //вычленяем из трека итоговый трек нужной длины и с нужным шагом между точками
    @NotNull
    private Object[] getPoints(Double trackLength, Integer trackStep, PointList pl) {
        DistanceCalcEarth distanceCalc = new DistanceCalcEarth();
        double currentTrackDistance = 0;
        double distanceBetweenFinalPoints = 0;
        double factFinishDistance = 0;
        double distanceBetweenTheNearestPoints = 0;
        GeometryFactory geometryFactory = new GeometryFactory();
        List<Point> resultingPointList = new ArrayList<>();
        for (int i = 0; i < pl.size(); i++) {
            if (i == 0) {
                Point point = geometryFactory.createPoint(new Coordinate(pl.getLat(i), pl.getLon(i)));
                resultingPointList.add(point);
                continue;
            }
            distanceBetweenTheNearestPoints = distanceCalc.calcDist(pl.getLat(i - 1), pl.getLon(i - 1), pl.getLat(i), pl.getLon(i));
            //distanceBetweenFinalPoints = distanceCalc.calcDist(prevLat, prevLon, pl.getLat(i), pl.getLon(i));
            distanceBetweenFinalPoints += distanceBetweenTheNearestPoints;


            if (distanceBetweenFinalPoints >= trackStep) {
//                System.out.println(distanceBetweenFinalPoints);
//                System.out.println(currentTrackDistance);
                currentTrackDistance += distanceBetweenFinalPoints;
                if (currentTrackDistance > trackLength) {
                    break;
                }
                Point point = geometryFactory.createPoint(new Coordinate(pl.getLat(i), pl.getLon(i)));
                resultingPointList.add(point);

                factFinishDistance = currentTrackDistance;
                distanceBetweenFinalPoints = 0;
//                System.out.println(currentTrackDistance);
//                System.out.println(factFinishDistance);
//                System.out.println();
            }
        }
        //return resultingPointList;
        return new Object[]{resultingPointList, factFinishDistance};
    }

    @Nullable
    private PointList getGraphhopperPointList(Double trackLength, String centralPoint) {
        String[] centralPointCoordinates = centralPoint.split(",");
        if (centralPointCoordinates.length != 2) {
            System.out.println("неверный формат координат, введите в виде 57.5555,82.1111");
            return null;
        }
        double latitude = Double.parseDouble(centralPointCoordinates[0]);
        double longitude = Double.parseDouble(centralPointCoordinates[1]);

        //вычисляем старт и финиш трека, для этого отступаем по широте относительно центральной точки влево/вправо по
        //половине заданной длины трека, для перевода в градусы полагаем, что находимся на широте Москвы, 1 мин шир = 1 км,
        //1 мин долг = 1.85 км - это в терминах 60 мин = гр, если мы работаем в формате координат 56.999, то 1 мин = 0.63 км
        GHPoint startPoint = new GHPoint(latitude, longitude - trackLength / (2 * METERS_TO_DEGREES));
        GHPoint finishPoint = new GHPoint(latitude, longitude + trackLength / (2 * METERS_TO_DEGREES));
        //это дело скорее всего надо будет переделать перед построением визуала. Надо будет брать круг или квадрат координат, брать случайные
        //точки и строить трек. Если трек малой - брать конечную точку, брать снова случайную точку еще и строить еще трек, слепить их и далее

        // specify at least two coordinates
        GHRequest request = new GHRequest().
                addPoint(startPoint).
                addPoint(finishPoint);

        request.setProfile("car");

//                // Optionally add path details
//        request.setPathDetails(Arrays.asList(
//                Parameters.Details.STREET_NAME,
//                Parameters.Details.,
//                Parameters.Details.AVERAGE_SPEED,
//                Parameters.Details.EDGE_ID
//        ));

        GHResponse fullRes = graphHopper.route(request);

        if (fullRes.hasErrors()) {
            System.out.println("ошибка при генерации трека на стороне Graphhopper");
            throw new RuntimeException(fullRes.getErrors().toString());
        }

        // get best path (you will get more than one path here if you requested algorithm=alternative_route)
//            ResponsePath res = fullRes.getBest();
        ResponsePath res = fullRes.getBest();
        // get path geometry information (latitude, longitude and optionally elevation)
        PointList pl = res.getPoints();
        // distance of the full path, in meter
        double distance = res.getDistance();
        System.out.println("INITIAL GENERATED DISTANCE =" + distance + " meters");
        // time of the full path, in milliseconds
        long millis = res.getTime();
        double minutes = (Math.round((millis / 60_000.0) * 100) / 100.0);
        System.out.println("Time = " + minutes + " minutes");
        System.out.println(pl.size());
        InstructionList il = res.getInstructions();
        System.out.println(il.size());
        return pl;

        //        // get information per turn instruction
//        InstructionList il = res.getInstructions();
//        for (Instruction i : il) {
//            // System.out.println(i.getName());
//
//            // to get the translated turn instructions you call:
//            // System.out.println(i.getTurnDescription(null));
//            // Note, that you can control the language only in via the request setLocale method and cannot change it only the client side
//        }
//        // get path details
//        List<PathDetail> pathDetails = res.getPathDetails().get(Parameters.Details.STREET_NAME);
//        for (PathDetail detail : pathDetails) {
////            System.out.println(detail.getValue());
//        }
    }

    @NotNull
    private String pointToAddress(Point point) {
//                GHGeocodingRequest request = new GHGeocodingRequest(pl.getLat(i), pl.getLon(i), "en", 1);
            GHGeocodingRequest request = new GHGeocodingRequest(true, new GHPoint(point.getX(), point.getY()),
                    null, "ru", 1, "opencagedata", -1L);

            GHGeocodingResponse response = geocoding.geocode(request);

            if (response.getHits() != null && !response.getHits().isEmpty()) {
                    GHGeocodingEntry location = response.getHits().get(0);
                return location.getName();
            } else {
                    return "Адрес не найден для данных координат";
            }
    }
}


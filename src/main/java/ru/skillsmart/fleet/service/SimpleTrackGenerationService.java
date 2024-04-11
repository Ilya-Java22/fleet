package ru.skillsmart.fleet.service;


import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;
import com.graphhopper.api.GraphHopperWeb;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.GHPoint;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillsmart.fleet.model.TrackPoint;
import ru.skillsmart.fleet.model.Trip;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.TrackPointRepository;
import ru.skillsmart.fleet.repository.TripRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class SimpleTrackGenerationService implements TrackGenerationService {

    @Value("${graphhopper.api.key}")
    private String graphHopperApiKey;

    private final TrackPointRepository trackPointRepository;
    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;

    public SimpleTrackGenerationService(TrackPointRepository trackPointRepository, TripRepository tripRepository, VehicleRepository vehicleRepository) {
        this.trackPointRepository = trackPointRepository;
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
    }

//???? в конфиге
//    @Bean
//    public String graphHopperApiKey() {
//        return graphHopperApiKey;
//    }
//}

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

        List<Point> resultingPointList = getPoints(trackLength, trackStep, pl);

        if (!resultingPointList.isEmpty()) {
//                Vehicle vehicle = new Vehicle();
//        		vehicle.setId(vehicleId);
            Vehicle vehicle = vehicleRepository.getReferenceById(vehicleId);
            Trip trip = new Trip();
            trip.setStartTime(LocalDateTime.now().withNano(0));
            trip.setVehicle(vehicle);
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
            tripRepository.save(trip);
        }
    }

    //вычленяем из трека итоговый трек нужной длины и с нужным шагом между точками
    @NotNull
    private List<Point> getPoints(Double trackLength, Integer trackStep, PointList pl) {
        DistanceCalcEarth distanceCalc = new DistanceCalcEarth();
        double prevLat = Double.NaN;
        double prevLon = Double.NaN;
        double currentTrackDistance = 0;
        double distanceBetweenPoints = 0;
        GeometryFactory geometryFactory = new GeometryFactory();
        List<Point> resultingPointList = new ArrayList<>();
        for (int i = 0; i < pl.size(); i++) {
            if (currentTrackDistance >= trackLength) {
                break;
            }
            if (i == 0) {
                prevLat = pl.getLat(0);
                prevLon = pl.getLon(0);
                Point point = geometryFactory.createPoint(new Coordinate(pl.getLat(i), pl.getLon(i)));
                resultingPointList.add(point);
                continue;
            }
            distanceBetweenPoints = distanceCalc.calcDist(prevLat, prevLon, pl.getLat(i), pl.getLon(i));

            if (distanceBetweenPoints >= trackStep) {
                Point point = geometryFactory.createPoint(new Coordinate(pl.getLat(i), pl.getLon(i)));
                resultingPointList.add(point);
                distanceBetweenPoints = 0;
                prevLat = pl.getLat(i);
                prevLon = pl.getLon(i);
            }

            currentTrackDistance = +distanceBetweenPoints;
        }
        return resultingPointList;
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

        GraphHopperWeb gh = new GraphHopperWeb();

        gh.setDownloader(new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build());

        gh.setKey(graphHopperApiKey);
        // change timeout, default is 5 seconds
        gh.setDownloader(new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build());

        // specify at least two coordinates
        GHRequest request = new GHRequest().
                addPoint(startPoint).
                addPoint(finishPoint);

        request.setProfile("car");

        //        // Optionally add path details
//        req.setPathDetails(Arrays.asList(
//                Parameters.Details.STREET_NAME,
//                Parameters.Details.AVERAGE_SPEED,
//                Parameters.Details.EDGE_ID
//        ));

        GHResponse fullRes = gh.route(request);

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
        long minutes = (Math.round(millis * 100) / 100) / 60_000;
        System.out.println("Time = " + minutes + " minutes");
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
}


package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;

import ru.skillsmart.fleet.model.Trip;
import ru.skillsmart.fleet.repository.TripRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.time.temporal.ChronoUnit.YEARS;

//выдает отчет в виде таблицы "период-пробег"
@Service
public class MileageReportService implements ReportService {

    public static final String NAME = "Отчет по пробегу автомобиля";
    public static final String VIEW = "reports/mileage";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getView() {
        return VIEW;
    }

    private final TripRepository tripRepository;

    public MileageReportService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Override
    public Map<String, Double> generateReport(int vehicleId, LocalDate startDate, LocalDate endDate, TemporalUnit periodUnit) {
        Map<String, Double> mileagePerPeriod = new LinkedHashMap<>();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Trip> trips = tripRepository.findAllTripsByVehicleIdAndTimeBetween(vehicleId, startDateTime, endDateTime);

        while (!startDateTime.isAfter(endDateTime)) {
            LocalDateTime periodEnd = startDateTime.plus(1, periodUnit).minusSeconds(1);
            double periodMileage = 0;

            for (Trip trip : trips) {
                if (trip.getStartTime().isBefore(periodEnd) && trip.getFinishTime().isAfter(startDateTime)) {
                    LocalDateTime effectiveStart = trip.getStartTime().isBefore(startDateTime) ? startDateTime : trip.getStartTime();
                    LocalDateTime effectiveEnd = trip.getFinishTime().isAfter(periodEnd) ? periodEnd : trip.getFinishTime();
                    double durationFraction = Duration.between(effectiveStart, effectiveEnd).getSeconds() / (double) Duration.between(trip.getStartTime(), trip.getFinishTime()).getSeconds();
                    periodMileage += durationFraction * trip.getDistanceInMeters();
                }
            }

            mileagePerPeriod.put(formatPeriod(startDateTime, periodUnit), Math.round(periodMileage * 100) * 1.0 / 100);
            startDateTime = startDateTime.plus(1, periodUnit);
        }

        return mileagePerPeriod;
    }

    //все юниты перечислены в интерфейсе ReportEngine
    private String formatPeriod(LocalDateTime dateTime, TemporalUnit periodUnit) {
        String periodUnitString = periodUnit.toString().toUpperCase();
        return switch (periodUnitString) {
            case "DAYS" -> dateTime.toLocalDate().toString();
            case "MONTHS" -> dateTime.getMonth() + " " + dateTime.getYear();
            case "YEARS" -> String.valueOf(dateTime.getYear());
            default -> throw new IllegalArgumentException("Unsupported temporal unit");
        };
    }
}

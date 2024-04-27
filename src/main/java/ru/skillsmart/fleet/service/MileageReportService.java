package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;

import ru.skillsmart.fleet.model.Report;
import ru.skillsmart.fleet.model.Trip;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.ReportRepository;
import ru.skillsmart.fleet.repository.TripRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//выдает отчет в виде таблицы "период-пробег"
@Service
public class MileageReportService implements ReportService {

    private static final String NAME = "Отчет по пробегу автомобиля";
    private static final String VIEW = "reports/mileage";
    private final ReportRepository reportRepository;
    private final TripRepository tripRepository;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getView() {
        return VIEW;
    }

    public MileageReportService(ReportRepository reportRepository, TripRepository tripRepository) {
        this.reportRepository = reportRepository;
        this.tripRepository = tripRepository;
    }

    private Map<String, Double> generateReport(int vehicleId, LocalDate startDate, LocalDate endDate, TemporalUnit periodUnit) {
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
    @Override
    public Optional<Report> createReport(Report report, TemporalUnit periodUnit) {
        Map<String, Double> results = generateReport(report.getVehicleId(), report.getStartDate(), report.getEndDate(), periodUnit);
        report.setResults(results);
        Report savedReport = reportRepository.save(report);
        return savedReport.getId() != 0 ? Optional.of(savedReport) : Optional.empty();
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

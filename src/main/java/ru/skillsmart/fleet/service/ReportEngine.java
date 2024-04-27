package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.dto.TrackPointDTO;
import ru.skillsmart.fleet.exception.ReportPeriodUnitException;
import ru.skillsmart.fleet.model.Report;
import ru.skillsmart.fleet.model.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ReportEngine {

    //switch обработка используется в реализации ReportService, в частности MileageRS, если добавляем - см там
    public static enum ReportPeriodUnit {
        DAYS("День", ChronoUnit.DAYS),
        MONTHS("Месяц", ChronoUnit.MONTHS),
        YEARS("Год", ChronoUnit.YEARS);

        private final String text;
        private final TemporalUnit unit;

        ReportPeriodUnit(String text, TemporalUnit unit) {
            this.text = text;
            this.unit = unit;
        }

        public TemporalUnit getUnit() {
            return unit;
        }

        public String getText() {
            return text;
        }
    }

    static ReportPeriodUnit[] getAvailablePeriodUnits() {
        return ReportPeriodUnit.values();
    }

    static TemporalUnit getReportPeriodUnitByName(String name) {
        try {
            ReportPeriodUnit unit = ReportPeriodUnit.valueOf(name);
            return unit.getUnit();
        } catch (IllegalArgumentException e) {
            throw new ReportPeriodUnitException("Invalid report period unit name: " + name);
        }
    }

    List<String> getAllReportsNames();
    Optional<Report> findById(int id);
    Optional<Report> createReport(Report report);
    List<Report> getSpecificReportsForVehicle(Integer vehicleId, LocalDate startDate, LocalDate endDate, String reportName, String reportPeriodUnit);

}

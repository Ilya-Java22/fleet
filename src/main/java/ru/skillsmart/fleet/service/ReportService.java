package ru.skillsmart.fleet.service;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Map;

public interface ReportService {
    String getName();
    String getView();
    Map<String, Double> generateReport(int vehicleId, LocalDate startDate, LocalDate endDate, TemporalUnit periodUnit);
}

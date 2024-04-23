package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SimpleReportEngine implements ReportEngine {

    private final Map<String, ReportService> reports = new HashMap<>();

    public SimpleReportEngine(Map<String, ReportService> reports) {
        reports.values().forEach(reportService -> this.reports.put(reportService.getName(), reportService));
    }

    @Override
    public List<String> getAllReportsNames() {
        return reports.values().stream()
                .map(ReportService::getName)
                .toList();
    }

    @Override
    public Map<String, Double> generateReport(int vehicleId, String reportName, LocalDate startDate, LocalDate endDate, String reportPeriodUnit) {

        TemporalUnit temporalReportPeriodUnit = ReportEngine.getReportPeriodUnitByName(reportPeriodUnit);

        return reports.get(reportName).generateReport(vehicleId, startDate, endDate, temporalReportPeriodUnit);
    }
}
package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.model.Report;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.ReportRepository;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.*;

@Service
public class SimpleReportEngine implements ReportEngine {

    private final Map<String, ReportService> reports = new HashMap<>();
    private final ReportRepository reportRepository;

    public SimpleReportEngine(Map<String, ReportService> reports, ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
        reports.values().forEach(reportService -> this.reports.put(reportService.getName(), reportService));
    }

    @Override
    public List<String> getAllReportsNames() {
        return reports.values().stream()
                .map(ReportService::getName)
                .toList();
    }

    @Override
    public Optional<Report> createReport(Report report) {
        TemporalUnit temporalReportPeriodUnit = ReportEngine.getReportPeriodUnitByName(report.getReportPeriodUnit());
        return reports.get(report.getReportName()).createReport(report, temporalReportPeriodUnit);
    }

    @Override
    public List<Report> getSpecificReportsForVehicle(Integer vehicleId, LocalDate startDate, LocalDate endDate, String reportName, String reportPeriodUnit) {
        List<Report> reports = reportRepository.findSpecificReportsForVehicle(vehicleId, startDate, endDate, reportName, reportPeriodUnit);
        return reports.isEmpty() ? new ArrayList<>() : reports;
    }

    @Override
    public Optional<Report> findById(int id) {
        return reportRepository.findById(id);
    }
}
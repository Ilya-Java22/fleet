package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.model.Report;

import java.time.LocalDate;
import java.time.temporal.TemporalUnit;
import java.util.Map;
import java.util.Optional;

public interface ReportService {
    String getName();
    String getView();
    Optional<Report> createReport(Report report, TemporalUnit periodUnit);
}

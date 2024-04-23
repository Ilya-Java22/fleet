package ru.skillsmart.fleet.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillsmart.fleet.exception.ReportPeriodUnitException;
import ru.skillsmart.fleet.service.ReportEngine;
import ru.skillsmart.fleet.service.ReportService;
import ru.skillsmart.fleet.service.VehicleService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ReportController {

    private final Map<String, String> reportViews = new HashMap<>();
    private final ReportEngine reportEngine;
    private final VehicleService vehicleService;

    public ReportController(Map<String, ReportService> reports, ReportEngine reportEngine, VehicleService vehicleService) {
        this.reportEngine = reportEngine;
        this.vehicleService = vehicleService;
        reports.values().forEach(reportService -> reportViews.put(reportService.getName(), reportService.getView()));
    }

    @GetMapping("/report")
    public String getReport(@RequestParam("vehicle_Id") int vehicleId,
                            @RequestParam("report") String reportName,
                            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                            @RequestParam("reportPeriodUnit") String reportPeriodUnit,
                            @RequestParam("enterpriseId") int enterpriseId,
                            Model model) {
        var vehicleOptional = vehicleService.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            model.addAttribute("message", "Машина с указанным идентификатором не найдена в базе");
            return "errors/404";
        }
        var vehicle = vehicleOptional.get();
        if (vehicle.getEnterprise().getId() != enterpriseId) {
            model.addAttribute("message", "Машина с указанным идентификатором не закреплена за данным предприятием");
            return "errors/404";
        }

        String viewName = reportViews.get(reportName);

        Path filePath = Paths.get("src/main/resources/templates/" + viewName + ".html");
        if (!Files.exists(filePath)) {
            model.addAttribute("message", "Не найдено отображение html для запрашиваемого отчета");
            return "errors/404";
        }

        Map<String, Double> report = reportEngine.generateReport(vehicleId, reportName, startDate, endDate, reportPeriodUnit);
        model.addAttribute("report", report);

        return viewName;
    }

    @ExceptionHandler(ReportPeriodUnitException.class)
    public String handleReportException(ReportPeriodUnitException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }
}

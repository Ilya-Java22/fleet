package ru.skillsmart.fleet.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skillsmart.fleet.exception.ReportPeriodUnitException;
import ru.skillsmart.fleet.model.Report;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.service.EnterpriseService;
import ru.skillsmart.fleet.service.ReportEngine;
import ru.skillsmart.fleet.service.ReportService;
import ru.skillsmart.fleet.service.VehicleService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ReportController {

    //мапа в виде "Отчет по пробегу автомобиля" (из соотв сервиса) - "вид"
    private final Map<String, String> reportViews = new HashMap<>();
    private final ReportEngine reportEngine;
    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;

    public ReportController(Map<String, ReportService> reports, ReportEngine reportEngine, VehicleService vehicleService, EnterpriseService enterpriseService) {
        this.reportEngine = reportEngine;
        this.vehicleService = vehicleService;
        this.enterpriseService = enterpriseService;
        reports.values().forEach(reportService -> reportViews.put(reportService.getName(), reportService.getView()));
    }

    @ExceptionHandler(ReportPeriodUnitException.class)
    public String handleReportException(ReportPeriodUnitException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "errors/404";
    }

    //первоначальный метод, когда отчет формировался на лету и сразу выдавался в html
//    @GetMapping("/report")
//    public String getReport(@RequestParam("vehicle_Id") int vehicleId,
//                            @RequestParam("report") String reportName,
//                            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
//                            @RequestParam("reportPeriodUnit") String reportPeriodUnit,
//                            @RequestParam("enterpriseId") int enterpriseId,
//                            Model model) {
//        var vehicleOptional = vehicleService.findById(vehicleId);
//        if (vehicleOptional.isEmpty()) {
//            model.addAttribute("message", "Машина с указанным идентификатором не найдена в базе");
//            return "errors/404";
//        }
//        var vehicle = vehicleOptional.get();
//        if (vehicle.getEnterprise().getId() != enterpriseId) {
//            model.addAttribute("message", "Машина с указанным идентификатором не закреплена за данным предприятием");
//            return "errors/404";
//        }
//
//        String viewName = reportViews.get(reportName);
//
//        Path filePath = Paths.get("src/main/resources/templates/" + viewName + ".html");
//        if (!Files.exists(filePath)) {
//            model.addAttribute("message", "Не найдено отображение html для запрашиваемого отчета");
//            return "errors/404";
//        }
//
//        Map<String, Double> report = reportEngine.generateReport(vehicleId, reportName, startDate, endDate, reportPeriodUnit);
//        model.addAttribute("report", report);
//
//        return viewName;
//    }
//
    @PostMapping("/report/create")
    public String createReport(@ModelAttribute Report report, @RequestParam("vehicle_Id") int vehicleId,
                               @RequestParam("enterpriseId") int enterpriseId,
                               Model model) {
        try {
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

            report.setVehicleId(vehicleId);

            Optional<Report> savedReport = reportEngine.createReport(report);
            if (savedReport.isEmpty()) {
                model.addAttribute("message", "Отчет не сформирован");
                return "errors/404";
            }
            model.addAttribute("message", "Отчет сформирован!");
            return "success/success";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/reports")
    public String getAll(Model model) {
        model.addAttribute("reports", reportEngine.getAllReportsNames());
        model.addAttribute("reportPeriodUnits", ReportEngine.getAvailablePeriodUnits());
        return "reports/list";
    }

    //используется в javascript на странице машины для выдачи отчетов
    @GetMapping("/api/reports")
    public ResponseEntity<Object> getSpecificReportsForVehicle(@RequestParam(name = "vehicleId") Integer vehicleId,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                               @RequestParam(name = "reportName") String reportName,
                                                               @RequestParam(name = "reportPeriodUnit") String reportPeriodUnit,
                                                               Principal principal) {
        Vehicle vehicle = vehicleService.findById(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle is not found. Please, check id."
                ));
        //забыл почему мы тут беспроблемно вытаскиваем ленивый enterprise))
        if (!enterpriseService.checkUserAccessToEnterprise(principal.getName(), vehicle.getEnterprise().getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        List<Report> reports = reportEngine.getSpecificReportsForVehicle(vehicleId, startDate, endDate, reportName, reportPeriodUnit);

        if (reports.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }


    @GetMapping("/report/{id}")
    public String getReport(Model model, @PathVariable int id, Principal principal) {
        var reportOptional = reportEngine.findById(id);
        if (reportOptional.isEmpty()) {
            model.addAttribute("message", "Данный отчет на данный момент не найден в базе");
            return "errors/404";
        }
        var report = reportOptional.get();

        String viewName = reportViews.get(report.getReportName());

        Path filePath = Paths.get("src/main/resources/templates/" + viewName + ".html");
        if (!Files.exists(filePath)) {
            model.addAttribute("message", "Не найдено отображение html для запрашиваемого отчета");
            return "errors/404";
        }

        model.addAttribute("report", report.getResults());

        return viewName;
    }

}

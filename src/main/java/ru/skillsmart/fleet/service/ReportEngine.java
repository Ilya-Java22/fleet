package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.exception.ReportPeriodUnitException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Map;
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
    Map<String, Double> generateReport(int vehicleId, String reportName, LocalDate startDate, LocalDate endDate, String reportPeriodUnit);

//    static final Map<String, ReportService> REPORT_SERVICES = new HashMap<>();
//
//    static {
//        REPORT_SERVICES.put("mileageReport", MileageReportService mileageReportService);
//    }
//
//
//
//    private final Map<String, ReportService> reportServices;
//
//    @Autowired
//    public ReportService(List<ReportService> reportServiceList) {
//        this.reportServices = reportServiceList.stream()
//                .collect(Collectors.toMap(service -> service.getClass().getSimpleName(),
//                        Function.identity()));
//    }




//    private static final Map<String, ReportService> REPORT_SERVICES = new HashMap<>();
//
//    @Autowired
//    private MileageReportService mileageReportService;
//
//    @PostConstruct
//    public void initReportServices() {
//        REPORT_SERVICES.put("mileageReport", mileageReportService);
//    }



//
//
//    public  Map<String, String> getReport(int vehicleId, String reportName) {
//        return REPORT_SERVICES.get(reportName).generateReport(vehicleId);
//    }
//
//    public abstract Map<String, String> generateReport(int vehicleId);

}

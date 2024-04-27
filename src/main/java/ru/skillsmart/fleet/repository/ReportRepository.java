package ru.skillsmart.fleet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.skillsmart.fleet.model.Report;
import ru.skillsmart.fleet.model.Trip;
import ru.skillsmart.fleet.model.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Integer> {

    Optional<Report> findById(int id);
    @Query("FROM Report r "
            + "WHERE r.vehicleId = :vehicleId "
            + "AND r.reportName = :reportName "
            + "AND r.reportPeriodUnit = :reportPeriodUnit "
            + "AND r.startDate >= :startDate AND r.endDate <= :endDate")
    List<Report> findSpecificReportsForVehicle(Integer vehicleId, LocalDate startDate, LocalDate endDate, String reportName, String reportPeriodUnit);

}

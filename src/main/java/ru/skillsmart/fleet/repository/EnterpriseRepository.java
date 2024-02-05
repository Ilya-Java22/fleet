package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Driver;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface EnterpriseRepository extends CrudRepository<Enterprise, Integer> {

    @Query("from Enterprise e LEFT JOIN FETCH e.vehiclesSet")
    List<Enterprise> findAllWithVehicles();

    @Query("from Enterprise e LEFT JOIN FETCH e.driversSet")
    List<Enterprise> findAllWithDrivers();

    @Query("from Enterprise e LEFT JOIN FETCH e.vehiclesSet where e.id = ?1")
    Optional<Enterprise> findByIdWithVehicles(Integer id);

    @Query("from Enterprise e LEFT JOIN FETCH e.driversSet where e.id = ?1")
    Optional<Enterprise> findByIdWithDrivers(Integer id);
}

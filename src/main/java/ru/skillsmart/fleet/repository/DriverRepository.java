package ru.skillsmart.fleet.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Driver;

import java.util.List;

public interface DriverRepository extends CrudRepository<Driver, Integer> {
    List<Driver> findAll();
}

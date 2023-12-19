package ru.skillsmart.fleet.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;

public interface BrandRepository extends CrudRepository<Brand, Integer> {
    List<Brand> findAll();
}

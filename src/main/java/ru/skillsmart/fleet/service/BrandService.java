package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> findAll();
    Optional<Brand> findById(int id);
    Brand getReferenceById(int id);
}

package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.model.Brand;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();
}

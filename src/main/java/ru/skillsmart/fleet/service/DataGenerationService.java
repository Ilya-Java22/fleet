package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.model.Enterprise;

import java.util.List;

public interface DataGenerationService {
    void generateData(List<Integer> enterprisesId, int numberOfVehicles, int numberOfDrivers);
}

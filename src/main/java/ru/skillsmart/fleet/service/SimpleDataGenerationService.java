package ru.skillsmart.fleet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import ru.skillsmart.fleet.model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class SimpleDataGenerationService implements DataGenerationService {

    @Autowired
    private BrandService brandService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private DriverService driverService;

    @Autowired
    private EnterpriseService enterpriseService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateData(List<Integer> enterprisesId, int numberOfVehicles, int numberOfDrivers) {
        enterprisesId.forEach(id -> generateDataForEnterpise(id, numberOfVehicles, numberOfDrivers));
    }

    private void generateDataForEnterpise(Integer enterpriseId, int numberOfVehicles, int numberOfDrivers) {
        int randomVehicleNumber = getRandomVehicleNumber(numberOfVehicles);
        for (int i = 0; i <= randomVehicleNumber; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setMileage(getRandomMileage());
            vehicle.setReleaseYear(getRandomYear());
            vehicle.setPrice(getRandomPrice());
            vehicle.setBrand(getRandomBrand());
            vehicle.setEnterprise(getEnterprise(enterpriseId));

            List<AssingingDriversToVehicles> listOfAssingingDriversToVehicles = new ArrayList<>();
            List<Driver> drivers = new ArrayList<>();
                int randomDriverNumber = getRandomDriverNumber(numberOfDrivers);
                for (int j = 0; j < randomDriverNumber; j++) {
                    Driver driver = new Driver();
                    driver.setName("driver".concat(Integer.toString(enterpriseId))
                            .concat(Integer.toString(i + 1))
                            .concat(Integer.toString(j + 1)));
                    driver.setSalary(getRandomDriverSalary());
                    driver.setEnterprise(getEnterprise(enterpriseId));
                    drivers.add(driver);
                }

            if (!drivers.isEmpty()) {
            driverService.saveAll(drivers);

            drivers.forEach(driver -> {
                AssingingDriversToVehicles assingingDriverToVehicle = new AssingingDriversToVehicles();
                assingingDriverToVehicle.setVehicle(vehicle);
                assingingDriverToVehicle.setDriver(driver);
                listOfAssingingDriversToVehicles.add(assingingDriverToVehicle);
            });

            vehicle.setAssingingDriversToVehicles(listOfAssingingDriversToVehicles);

            if (i % 10 == 0) {
                vehicle.setActiveDriver(listOfAssingingDriversToVehicles
                        .get(ThreadLocalRandom.current().nextInt(listOfAssingingDriversToVehicles.size()))
                        .getDriver());
            }

            }
            vehicleService.save(vehicle);
            }
    }

    private Enterprise getEnterprise(Integer enterpriseId) {
        return enterpriseService.getReferenceById(enterpriseId);
    }

    private int getRandomYear() {
        return ThreadLocalRandom.current().nextInt(1990, 2023);
    }

    private int getRandomVehicleNumber(int numberOfVehicles) {
        return ThreadLocalRandom.current().nextInt(3000, numberOfVehicles + 1);
    }

    private int getRandomDriverNumber(int numberOfDrivers) {
        return ThreadLocalRandom.current().nextInt(1, numberOfDrivers + 1);
    }

    private double getRandomPrice() {
        double price = ThreadLocalRandom.current().nextDouble(50000, 200000);
        return Math.round(price * 100) * 1.0 / 100;
    }

    private int getRandomMileage() {
        return ThreadLocalRandom.current().nextInt(200000);
    }

    private Brand getRandomBrand() {
        int brandIndex = ThreadLocalRandom.current().nextInt(1, 4);
        return brandService.getReferenceById(brandIndex);
    }

    private double getRandomDriverSalary() {
        double salary = ThreadLocalRandom.current().nextDouble(100, 200);
        return Math.round(salary * 100) * 1.0 / 100;
    }
}

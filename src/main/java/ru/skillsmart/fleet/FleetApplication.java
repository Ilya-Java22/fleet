package ru.skillsmart.fleet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.skillsmart.fleet.model.*;
import ru.skillsmart.fleet.repository.AssingingDriversToVehiclesRepository;
import ru.skillsmart.fleet.repository.DriverRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;
import ru.skillsmart.fleet.service.EnterpriseService;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class FleetApplication {
	private static VehicleRepository vehicleRepository;
	private static DriverRepository driverRepository;
	private static EnterpriseService enterpriseService;
	private static AssingingDriversToVehiclesRepository assingingDriversToVehiclesRepository;

	//public static TransactionManager t;

	public FleetApplication(VehicleRepository vehicleRepository, DriverRepository driverRepository,
							EnterpriseService enterpriseService, AssingingDriversToVehiclesRepository assingingDriversToVehiclesRepository) {
		this.vehicleRepository = vehicleRepository;
		this.driverRepository = driverRepository;
		this.enterpriseService = enterpriseService;
		this.assingingDriversToVehiclesRepository = assingingDriversToVehiclesRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(FleetApplication.class, args);
//		Driver driver1 = driverRepository.findById(2).get();
//		Driver driver2 = driverRepository.findById(12).get();
//		Driver driver3 = driverRepository.findById(13).get();
//		Driver driver4 = driverRepository.findById(14).get();
//		Driver driver5 = driverRepository.findById(15).get();
//		Driver driver6 = driverRepository.findById(16).get();
//		Vehicle vehicle1 = vehicleRepository.findById(1).get();
//		Vehicle vehicle4 = vehicleRepository.findById(4).get();
//		Vehicle vehicle5 = vehicleRepository.findById(5).get();
//		Vehicle vehicle7 = vehicleRepository.findById(11).get();
////		vehicle1.setActiveDriver(driver1);
////		vehicle5.setActiveDriver(driver3);
////		vehicleRepository.save(vehicle1);
////		vehicleRepository.save(vehicle5);
//		AssingingDriversToVehicles a1 = new AssingingDriversToVehicles();
//		a1.setDriver(driver1);
//		a1.setVehicle(vehicle1);
//		AssingingDriversToVehicles a2 = new AssingingDriversToVehicles();
//		a2.setDriver(driver5);
//		a2.setVehicle(vehicle1);
//		AssingingDriversToVehicles a3 = new AssingingDriversToVehicles();
//		a3.setDriver(driver2);
//		a3.setVehicle(vehicle4);
//		AssingingDriversToVehicles a4 = new AssingingDriversToVehicles();
//		a4.setDriver(driver3);
//		a4.setVehicle(vehicle4);
//		AssingingDriversToVehicles a5 = new AssingingDriversToVehicles();
//		a5.setDriver(driver3);
//		a5.setVehicle(vehicle5);
//		AssingingDriversToVehicles a6 = new AssingingDriversToVehicles();
//		a6.setDriver(driver6);
//		a6.setVehicle(vehicle5);
//		AssingingDriversToVehicles a7 = new AssingingDriversToVehicles();
//		a7.setDriver(driver4);
//		a7.setVehicle(vehicle7);
//		assingingDriversToVehiclesRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6, a7));
		Vehicle vehicle1 = vehicleRepository.findById(1).get();
		//vehicleRepository.delete(vehicle1);
	}
}

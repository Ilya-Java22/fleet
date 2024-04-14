package ru.skillsmart.fleet;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.skillsmart.fleet.model.*;
import ru.skillsmart.fleet.repository.AssingingDriversToVehiclesRepository;
import ru.skillsmart.fleet.repository.DriverRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;
import ru.skillsmart.fleet.service.EnterpriseService;

import java.time.LocalDateTime;
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
		//эти выражения для сохранения с 19 коммита не релевантны, мы теперь сохраняем поездки, не точки
////		GeometryFactory geometryFactory = new GeometryFactory();
//		int srid = 4326; // Пример SRID для WGS 84
//		GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
//		// Предположим, что у вас есть объект Vehicle, к которому относится трек-точка, установите его здесь
//		Vehicle vehicle = new Vehicle();
//		vehicle.setId(1); // Пример установки ID
//
//		Point point1 = geometryFactory.createPoint(new Coordinate(-73.9857, 40.7484));
//		TrackPoint trackPoint1 = new TrackPoint();
//		trackPoint1.setTime(LocalDateTime.of(2024, 02, 28, 8, 00, 00));
//		trackPoint1.setPoint(point1);
//		trackPoint1.setVehicle(vehicle);
//
//		Point point2 = geometryFactory.createPoint(new Coordinate(-73.9876, 40.7491));
//		TrackPoint trackPoint2 = new TrackPoint();
//		trackPoint2.setTime(LocalDateTime.of(2024, 02, 28, 8, 15, 00));
//		trackPoint2.setPoint(point2);
//		trackPoint2.setVehicle(vehicle);
//
//		Point point3 = geometryFactory.createPoint(new Coordinate(-73.9899, 40.7499));
//		TrackPoint trackPoint3 = new TrackPoint();
//		trackPoint3.setTime(LocalDateTime.of(2024, 02, 28, 8, 17, 00));
//		trackPoint3.setPoint(point3);
//		trackPoint3.setVehicle(vehicle);
//
//		Point point4 = geometryFactory.createPoint(new Coordinate(-73.9911, 40.7503));
//		TrackPoint trackPoint4 = new TrackPoint();
//		trackPoint4.setTime(LocalDateTime.of(2024, 02, 28, 8, 20, 00));
//		trackPoint4.setPoint(point4);
//		trackPoint4.setVehicle(vehicle);
//
//		Point point5 = geometryFactory.createPoint(new Coordinate(-73.9999, 40.7525));
//		TrackPoint trackPoint5 = new TrackPoint();
//		trackPoint5.setTime(LocalDateTime.of(2024, 02, 28, 8, 25, 00));
//		trackPoint5.setPoint(point5);
//		trackPoint5.setVehicle(vehicle);
//
//		Point point6 = geometryFactory.createPoint(new Coordinate(-75.9811, 40.7465));
//		TrackPoint trackPoint6 = new TrackPoint();
//		trackPoint6.setTime(LocalDateTime.of(2024, 02, 28, 20, 29, 00));
//		trackPoint6.setPoint(point6);
//		trackPoint6.setVehicle(vehicle);
//
//		Point point7 = geometryFactory.createPoint(new Coordinate(-75.9611, 40.7365));
//		TrackPoint trackPoint7 = new TrackPoint();
//		trackPoint7.setTime(LocalDateTime.of(2024, 02, 28, 20, 35, 00));
//		trackPoint7.setPoint(point7);
//		trackPoint7.setVehicle(vehicle);
//
//		Point point8 = geometryFactory.createPoint(new Coordinate(-73.9857, 40.7484));
//		TrackPoint trackPoint8 = new TrackPoint();
//		trackPoint8.setTime(LocalDateTime.of(2024, 02, 28, 20, 41, 00));
//		trackPoint8.setPoint(point8);
//		trackPoint8.setVehicle(vehicle);
//
//		Point point9 = geometryFactory.createPoint(new Coordinate(-73.9857, 40.7484));
//		TrackPoint trackPoint9 = new TrackPoint();
//		trackPoint9.setTime(LocalDateTime.of(2024, 02, 28, 20, 49, 00));
//		trackPoint9.setPoint(point9);
//		trackPoint9.setVehicle(vehicle);
//
//		Point point10 = geometryFactory.createPoint(new Coordinate(-75.9011, 40.7065));
//		TrackPoint trackPoint10 = new TrackPoint();
//		trackPoint10.setTime(LocalDateTime.of(2024, 02, 28, 20, 55, 00));
//		trackPoint10.setPoint(point10);
//		trackPoint10.setVehicle(vehicle);
//
//		trackPointRepository.saveAll(List.of(trackPoint10, trackPoint9, trackPoint8, trackPoint7, trackPoint6, trackPoint5, trackPoint4, trackPoint3, trackPoint2, trackPoint1));
//

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
		//vehicleRepository.delete(vehicle1);

		//Vehicle vehicle1 = vehicleRepository.findById(1).get();
	}
}

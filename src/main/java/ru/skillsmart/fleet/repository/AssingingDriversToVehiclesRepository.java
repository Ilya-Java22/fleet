package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.AssingingDriversToVehicles;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;

public interface AssingingDriversToVehiclesRepository extends CrudRepository<AssingingDriversToVehicles, Integer> {
//    @Query("from Vehicle v JOIN FETCH v.brand")
//    List<Vehicle> findAll();
//
//    @Modifying
//    @Query("delete from Vehicle v where v.id = ?1")
//    int deletebyId(Integer id);
}

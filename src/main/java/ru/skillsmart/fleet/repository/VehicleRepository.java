package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {
    @Query("from Vehicle v JOIN FETCH v.brand")
    List<Vehicle> findAll();

    @Modifying
    @Query("delete from Vehicle v where v.id = ?1")
    int deletebyId(Integer id);
}

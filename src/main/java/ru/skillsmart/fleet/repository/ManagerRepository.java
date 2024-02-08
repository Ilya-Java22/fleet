package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillsmart.fleet.model.Manager;
import ru.skillsmart.fleet.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    //@Query("from Manager m JOIN FETCH m.enterprises where m.username = ?1")
    //@Query("from Manager m where m.username = ?1")
    /*    скорее всего это очень плохо, у нас декартово произведение. Лучше будет, думаю, вытащить нужные id предприятий,
        потом в этом диапазоне выбрать предприятия с водителями, потом предприятия с машинами и объединить в @Transaction
        в сервисном слое.*/
    @EntityGraph(attributePaths = {"enterprises.driversSet", "enterprises.vehiclesSet"})
    Manager findManagerByUsername(String username);

}

package ru.skillsmart.fleet.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillsmart.fleet.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    //@Query("from Manager m JOIN FETCH m.enterprises where m.username = ?1")
    //@Query("from Manager m where m.username = ?1")
    /*    скорее всего это очень плохо, у нас декартово произведение. Лучше будет, думаю, вытащить нужные id предприятий,
        потом в этом диапазоне выбрать предприятия с водителями, потом предприятия с машинами и объединить в @Transaction
        в сервисном слое.*/
    //@EntityGraph(attributePaths = {"enterprises.driversSet", "enterprises.vehiclesSet"})
    @EntityGraph(attributePaths = "authorities")
    User findUserByUsername(String username);

    @EntityGraph(attributePaths = {"enterprises.driversSet", "enterprises.vehiclesSet"})
    User findUserWithEnterprisesWithDriversVehiclesByUsername(String username);

    @EntityGraph(attributePaths = "enterprises")
    User findUserWithEnterprisesByUsername(String username);

}

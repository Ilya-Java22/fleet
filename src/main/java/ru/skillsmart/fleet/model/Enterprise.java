package ru.skillsmart.fleet.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "enterprises")
public class Enterprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String name;
    private String city;

    @Column(columnDefinition = "VARCHAR DEFAULT 'UTC'")
    private String timezone;

    //еще preupdate есть
    @PrePersist
    public void prePersist() {
        if (this.timezone == null) {
            this.timezone = "UTC";
        }
    }

//    @JoinColumn(name = "enterprise_id") если бы была односторонняя связь (в Driver нет поля Е, но есть столбец,
//    этого бы плюс просто @OneToMany хватило, но делаю двустороннюю, так как сохраняю машину с предприятием, а не наоборот (см. генерацию данных)
// И! односторонняя onetomany это плохо) лишние запросы https://sysout.ru/otnoshenie-onetomany-v-hibernate-i-spring/
    @OneToMany(mappedBy = "enterprise", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Driver> driversSet = new HashSet<>();

    //@JsonIgnore
    @OneToMany(mappedBy = "enterprise", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Vehicle> vehiclesSet = new HashSet<>();

    public void addVehicles(Vehicle... vehicles) {
        for (Vehicle vehicle : vehicles) {
            this.vehiclesSet.add(vehicle);
            vehicle.setEnterprise(this);
        }
    }

    public void removeVehicles(Vehicle... vehicles) {
        for (Vehicle vehicle : vehicles) {
            vehicle.setEnterprise(null);
            this.vehiclesSet.remove(vehicle);
        }
    }
}

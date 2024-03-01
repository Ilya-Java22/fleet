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

    //@JsonIgnore
    @OneToMany
    @JoinColumn(name = "enterprise_id")
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

package ru.skillsmart.fleet.model;

import jakarta.persistence.*;
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
    private int id;

    private String name;
    private String city;

    @OneToMany
    @JoinColumn(name = "enterprise_id")
    private Set<Driver> driversSet = new HashSet<>();

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

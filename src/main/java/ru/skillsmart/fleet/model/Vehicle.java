package ru.skillsmart.fleet.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "release_year")
    private int releaseYear;
    private int mileage;
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<AssingingDriversToVehicles> assingingDriversToVehicles = new HashSet<>();

//    @PreRemove
//    public void remove() {
//        assingingDriversToVehicles.forEach(position -> {
//            position.setVehicle(null);
//            position.setDriver(null);
//        });
//    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "active_drivers_vehicles",
            joinColumns =
                    { @JoinColumn(name = "vehicle_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "driver_id", referencedColumnName = "id") })
    private Driver activeDriver;
}
package ru.skillsmart.fleet.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private LocalDateTime purchased;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssingingDriversToVehicles> assingingDriversToVehicles = new ArrayList<>();

//    @PreRemove
//    public void remove() {
//        assingingDriversToVehicles.forEach(position -> {
//            position.setVehicle(null);
//            position.setDriver(null);
//        });
//    }


    //all - видимо, хочет сохр driver в drivers, но там уже есть, значит, удалит из drivers
    //односторонняя связь - всё просто, удаляем vehicle, active спокойно удаляется, с assinging такое не проходит
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "active_drivers_vehicles",
            joinColumns =
                    { @JoinColumn(name = "vehicle_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "driver_id", referencedColumnName = "id") })
    private Driver activeDriver;
}
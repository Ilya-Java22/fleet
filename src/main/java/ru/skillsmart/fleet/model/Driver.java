package ru.skillsmart.fleet.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;
    private double salary;

//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "active_vehicle_id")
//    private Vehicle activeVehicle;
}

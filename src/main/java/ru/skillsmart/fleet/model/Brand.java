package ru.skillsmart.fleet.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String brand;
    private String type;
    @Column(name = "tank_capacity")
    private int tankCapacity;
    @Column(name = "lifting_capacity")
    private int liftingCapacity;
    private int capacity;
}
package ru.skillsmart.fleet.model;

import javax.persistence.*;
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

    @Override
    public String toString() {
        return "Марка '" + brand + '\''
                + ", тип кузова'" + type + '\''
                + ", литраж (л) " + tankCapacity
                + ", грузоподъемность (тонн) " + liftingCapacity
                + ", вместимость (чел) " + capacity;
    }
}
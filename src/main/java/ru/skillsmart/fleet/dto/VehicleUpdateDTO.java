package ru.skillsmart.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleUpdateDTO {
    private int id;
    private int releaseYear;
    private int mileage;
    private double price;
    private int brandId;
    private int enterpriseId;
    private int activeDriverId;
}

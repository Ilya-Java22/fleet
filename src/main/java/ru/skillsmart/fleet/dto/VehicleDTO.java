package ru.skillsmart.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private int id;
    private int releaseYear;
    private int mileage;
    private double price;
    private int brandId;
    private int enterpriseId;
    private int activeDriverId;
}

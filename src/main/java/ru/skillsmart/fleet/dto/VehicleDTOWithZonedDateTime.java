package ru.skillsmart.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTOWithZonedDateTime {
    private int id;
    private int releaseYear;
    private int mileage;
    private double price;
    private int brandId;
    private int enterpriseId;
    private String enterpriseTimeZone;
    private int activeDriverId;
    private ZonedDateTime purchased;
}

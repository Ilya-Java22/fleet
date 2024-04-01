package ru.skillsmart.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCreateDTO {
    private int releaseYear;
    private int mileage;
    private double price;
    private int brandId;
    private int enterpriseId;
    //c форматом @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") не рабит
    //c форматом @DateTimeFormat(pattern = "YYYY-MM-DD HH24:MI:SS") не рабит
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime purchased;
}

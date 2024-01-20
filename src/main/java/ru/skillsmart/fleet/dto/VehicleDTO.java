package ru.skillsmart.fleet.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillsmart.fleet.model.Brand;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private int id;
    private int releaseYear;
    private int mileage;
    private double price;
    private int brandId;
}

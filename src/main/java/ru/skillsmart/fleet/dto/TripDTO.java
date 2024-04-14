package ru.skillsmart.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripDTO {
    private int tripId;
    private int vehicleId;
    private double distanceInMeters;
    private LocalDateTime startTime;
    private double startLat;
    private double startLon;
    private String startAddress;
    private LocalDateTime finishTime;
    private double finishLat;
    private double finishLon;
    private String finishAddress;
}


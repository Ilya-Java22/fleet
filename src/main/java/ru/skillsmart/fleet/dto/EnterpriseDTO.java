package ru.skillsmart.fleet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseDTO {
    private int id;
    private String name;
    private String city;
    //при небольшом размере коллекций ок, при больших - скорее всего надо убрать
    private Set<Integer> driversIdSet;
    private Set<Integer> vehiclesIdSet;
}

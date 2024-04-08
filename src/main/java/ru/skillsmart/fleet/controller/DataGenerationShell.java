package ru.skillsmart.fleet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.service.DataGenerationService;
import ru.skillsmart.fleet.service.EnterpriseService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class DataGenerationShell {

    @Autowired
    private DataGenerationService dataGenerationService;

    @ShellMethod("Generate data for enterprises")
    //public String generateDataForEnterprises(String enterpriseIds, int numberOfVehicles, int numberOfDrivers) {
    public String generateData(
            @ShellOption(help = "List of enterprise IDs") List<Integer> enterprisesId,
            @ShellOption(help = "Max number of vehicles") int numberOfVehicles,
            @ShellOption(help = "Max number of drivers") int numberOfDrivers) {
//        List<Enterprise> enterprises = enterpriseService.findAllById(enterpriseIds.split(","));
//        List<Enterprise> enterprises = Arrays.stream(enterpriseIds.split(","))
//                .map(id -> {
//                    enterpriseService.getReferenceById(Integer.parseInt(id));
//                })
//                .collect(Collectors.toList());
//        List<Enterprise> enterprises = enterprisesId.stream()
//                .map(enterpriseService::getReferenceById)
//                .toList();
        dataGenerationService.generateData(enterprisesId, numberOfVehicles, numberOfDrivers);
        return "Данные сгенерированы для предприятий: " + enterprisesId;
    }
}

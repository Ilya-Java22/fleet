package ru.skillsmart.fleet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.service.BrandService;
import ru.skillsmart.fleet.service.EnterpriseService;
import ru.skillsmart.fleet.service.VehicleService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/enterprises")
public class EnterpriseController {

    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;

    public EnterpriseController(VehicleService vehicleService, EnterpriseService enterpriseService) {
        this.vehicleService = vehicleService;
        this.enterpriseService = enterpriseService;
    }

    @GetMapping(path = "/", produces = "application/json")
    public @ResponseBody List<EnterpriseDTO> findAll() {
        return this.enterpriseService.findAllDto();
    }
}
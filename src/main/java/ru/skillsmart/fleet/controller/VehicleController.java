package ru.skillsmart.fleet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillsmart.fleet.service.SimpleVehicleService;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final SimpleVehicleService vehicleService;

    public VehicleController(SimpleVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vehicles", vehicleService.findAll());
        return "vehicles/list";
    }
}
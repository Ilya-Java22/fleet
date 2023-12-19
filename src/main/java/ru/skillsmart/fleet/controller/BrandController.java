package ru.skillsmart.fleet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillsmart.fleet.service.SimpleBrandService;
import ru.skillsmart.fleet.service.SimpleVehicleService;

@Controller
@RequestMapping("/brands")
public class BrandController {

    private final SimpleBrandService brandService;

    public BrandController(SimpleBrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "brands/list";
    }
}

package ru.skillsmart.fleet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.service.BrandService;
import ru.skillsmart.fleet.service.VehicleService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final BrandService brandService;

    public VehicleController(VehicleService vehicleService, BrandService brandService) {
        this.vehicleService = vehicleService;
        this.brandService = brandService;
    }

    @GetMapping(path = "/", produces = "application/json")
    public @ResponseBody List<VehicleDTO> findAll() {
        return this.vehicleService.findAllDto();
    }

    @GetMapping
    public String getAll(Model model) {
        List<Vehicle> vehicleList = vehicleService.findAll();
        vehicleList.sort(Comparator.comparing(Vehicle::getId));
        model.addAttribute("vehicles", vehicleList);
        return "vehicles/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "vehicles/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Vehicle vehicle, @RequestParam("brand.id") int brandId, Model model) {
        try {
            vehicle.setBrand(brandService.findById(brandId).get());
            vehicleService.save(vehicle);
            return "redirect:/vehicles";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vehicle vehicle, @RequestParam("brand.id") int brandId, Model model) {
        try {
            var isUpdated = vehicleService.update(vehicle);
            if (!isUpdated) {
                model.addAttribute("message", "Машина с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/vehicles";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var vehicleOptional = vehicleService.findById(id);
        if (vehicleOptional.isEmpty()) {
            model.addAttribute("message", "Машина с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("vehicle", vehicleOptional.get());
        return "vehicles/one";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = vehicleService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Машина с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vehicles";
    }
}
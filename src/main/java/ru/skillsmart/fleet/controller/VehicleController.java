package ru.skillsmart.fleet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.skillsmart.fleet.dto.VehicleCreateDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.service.BrandService;
import ru.skillsmart.fleet.service.EnterpriseService;
import ru.skillsmart.fleet.service.UserService;
import ru.skillsmart.fleet.service.VehicleService;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//@RequestMapping("/vehicle")
@Controller
public class VehicleController {

    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final EnterpriseService enterpriseService;

    public VehicleController(VehicleService vehicleService, BrandService brandService, UserService userService, EnterpriseService enterpriseService) {
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.enterpriseService = enterpriseService;
    }

    @GetMapping(path = "/api/vehicle/", produces = "application/json")
    public @ResponseBody List<VehicleDTO> findAll() {
        return this.vehicleService.findAllDto();
    }

    @GetMapping(path = "/vehicle")
    public String getAll(Model model) {
        List<Vehicle> vehicleList = vehicleService.findAll();
        vehicleList.sort(Comparator.comparing(Vehicle::getId));
        model.addAttribute("vehicles", vehicleList);
        return "vehicles/list";
    }


    @GetMapping("/vehicle/{id}")
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


    @GetMapping("/vehicle/create")
    public String getCreationPage(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "vehicles/create";
    }

    //@PreAuthorize("hasAuthority('MANAGER')")
    @ResponseBody
    @PostMapping(value = "/api/vehicle/", consumes = "application/json")
    public  ResponseEntity<VehicleDTO> createRest(@RequestBody VehicleCreateDTO vehicleCreateDTO, Principal principal) {
        if (!enterpriseService.checkUserAccessToEnterprise(principal.getName(), vehicleCreateDTO.getEnterpriseId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return vehicleService.saveRest(vehicleCreateDTO)
                .map(x -> new ResponseEntity<>(x, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PostMapping("/vehicle/create")
    public String create(@ModelAttribute Vehicle vehicle, @RequestParam("brand.id") int brandId, Model model) {
        try {
            vehicle.setBrand(brandService.findById(brandId).get());
            vehicleService.save(vehicle);
            return "redirect:/vehicle";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/vehicle/update")
    public String update(@ModelAttribute Vehicle vehicle, @RequestParam("brand.id") int brandId, Model model) {
        try {
            var isUpdated = vehicleService.update(vehicle);
            if (!isUpdated) {
                model.addAttribute("message", "Машина с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/vehicle";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @PutMapping("/api/vehicle/{vehicleId}/move-to/{newEnterpriseId}")
    public ResponseEntity<?> moveVehicleToEnterprise(@PathVariable Integer vehicleId, @PathVariable Integer newEnterpriseId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Vehicle vehicle = vehicleService.findById(vehicleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Vehicle is not found. Please, check id."
                ));
        if (!enterpriseService.checkUserAccessToEnterprise(userDetails.getUsername(), vehicle.getEnterprise().getId(), newEnterpriseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access to Enterprise Denied");
        }
        if (!vehicle.getAssingingDriversToVehicles().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The car has a driver assigned. Moving is not possible.");
        }
        vehicleService.moveVehicleToEnterprise(vehicle, newEnterpriseId);
        return ResponseEntity.ok("Vehicle moved to a new Enterprise successfully.");
        //return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to save Vehicle");
    }

    @GetMapping("/vehicle/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = vehicleService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "Машина с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vehicle";
    }
}
package ru.skillsmart.fleet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.User;
import ru.skillsmart.fleet.service.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseController {

    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;
    private final UserService userService;

    public EnterpriseController(VehicleService vehicleService, EnterpriseService enterpriseService, UserService userService) {
        this.vehicleService = vehicleService;
        this.enterpriseService = enterpriseService;
        this.userService = userService;
    }

//    @GetMapping(path = "/1", produces = "application/json")
//    public @ResponseBody List<EnterpriseDTO> findAll() {
//        return this.enterpriseService.findAllDto();
//    }

    @GetMapping(path = "/", produces = "application/json")
    public List<EnterpriseDTO> getEnterprisesForManager(Principal principal) {
        String username = principal.getName();
        List<EnterpriseDTO> enterprises = enterpriseService.findUserEnterprisesWithDriversVehiclesDto(username);
        return enterprises;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnterpriseDTO> findById(@PathVariable int id, Principal principal) {
        if (!enterpriseService.checkUserAccessToEnterprise(principal.getName(), id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return enterpriseService.findById(id).map(ResponseEntity::ok).get();
    }
}
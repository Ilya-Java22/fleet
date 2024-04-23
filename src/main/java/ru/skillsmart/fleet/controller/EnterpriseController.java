package ru.skillsmart.fleet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillsmart.fleet.dto.EnterpriseDTO;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.service.*;

import java.security.Principal;
import java.util.List;

@Controller
public class EnterpriseController {

    private final VehicleService vehicleService;
    private final EnterpriseService enterpriseService;
    private final UserService userService;
    private final ReportEngine reportEngine;

    public EnterpriseController(VehicleService vehicleService, EnterpriseService enterpriseService, UserService userService, ReportEngine reportEngine) {
        this.vehicleService = vehicleService;
        this.enterpriseService = enterpriseService;
        this.userService = userService;
        this.reportEngine = reportEngine;
    }

//    @GetMapping(path = "/1", produces = "application/json")
//    public @ResponseBody List<EnterpriseDTO> findAll() {
//        return this.enterpriseService.findAllDto();
//    }

    @GetMapping("/enterprise/{id}")
    public String getById(Model model, @PathVariable int id, @RequestParam("enterpriseName") String enterpriseName) {
//        var enterprisOptional = enterpriseService.findById(id);
//        if (enterprisOptional.isEmpty()) {
//            model.addAttribute("message", "Предприятие с указанным идентификатором не найдено");
//            return "errors/404";
//        }
//        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("reports", reportEngine.getAllReportsNames());
        model.addAttribute("reportPeriodUnits", ReportEngine.getAvailablePeriodUnits());
        model.addAttribute("enterpriseName", enterpriseName);
        model.addAttribute("enterpriseId", id);
        return "enterprises/one";
    }
    @GetMapping(path = "/enterprise")
    public String getAll(Model model, Principal principal) {
        String username = principal.getName();
        List<Enterprise> enterpriseList = enterpriseService.findUserEnterprises(username);
        model.addAttribute("enterprises", enterpriseList);
        return "enterprises/list";
    }

    //при большом кол-ве машин и водителей надо бы переделать на пагинацию либо убрать поля из dto
    @ResponseBody
    @GetMapping(path = "/api/enterprise", produces = "application/json")
    public List<EnterpriseDTO> getEnterprisesForManager(Principal principal) {
        String username = principal.getName();
        List<EnterpriseDTO> enterprises = enterpriseService.findUserEnterprisesWithDriversVehiclesDto(username);
        return enterprises;
    }

    @ResponseBody
    @GetMapping("/api/enterprise/{id}")
    public ResponseEntity<EnterpriseDTO> findById(@PathVariable int id, Principal principal) {
        if (!enterpriseService.checkUserAccessToEnterprise(principal.getName(), id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return enterpriseService.findById(id).map(ResponseEntity::ok).get();
    }
}
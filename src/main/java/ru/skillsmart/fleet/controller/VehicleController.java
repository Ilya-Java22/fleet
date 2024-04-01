package ru.skillsmart.fleet.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
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
import ru.skillsmart.fleet.dto.VehicleUpdateDTO;
import ru.skillsmart.fleet.mapper.EnterpriseMapper;
import ru.skillsmart.fleet.mapper.VehicleMapper;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.service.*;
import ru.skillsmart.fleet.utility.TimeZoneUtility;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@RequestMapping("/vehicle")
@Controller
public class VehicleController {

    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final EnterpriseService enterpriseService;

    private final DriverService driverService;

    public VehicleController(VehicleService vehicleService, BrandService brandService, UserService userService, EnterpriseService enterpriseService, EnterpriseMapper enterpriseMapper, VehicleMapper vehicleMapper, DriverService driverService) {
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.enterpriseService = enterpriseService;
        this.driverService = driverService;
    }

//    заменил на пагинацю, см. ниже
//    @GetMapping(path = "/api/vehicle/", produces = "application/json")
//    public @ResponseBody List<VehicleDTO> findAll() {
//        return this.vehicleService.findAllDto();
//    }

    @GetMapping(path = "/api/vehicle/", produces = "application/json")
    public @ResponseBody Page<VehicleDTO> findAll(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") @Min(1) @Max(100) Integer limit
    ) {
        return this.vehicleService.findAllDto(PageRequest.of(offset, limit));
    }

    // убрал, это старый метод, когда кол-ва машин немного
//    @GetMapping(path = "/vehicle")
//    public String getAll(Model model) {
//        List<Vehicle> vehicleList = vehicleService.findAll();
//        vehicleList.sort(Comparator.comparing(Vehicle::getId));
//        model.addAttribute("vehicles", vehicleList);
//        return "vehicles/list";
//    }


    @GetMapping("/vehicle/{id}")
    public String getById(Model model, @PathVariable int id, @RequestParam("enterpriseId") int enterpriseId,
                          @RequestParam("enterpriseName") String enterpriseName, Principal principal, HttpServletRequest request) {
        //getReffered?
        var vehicleDTOZonedDTOptional = vehicleService.findVehicleDTOWithZonedDateTimeById(id);
        if (vehicleDTOZonedDTOptional.isEmpty()) {
            model.addAttribute("message", "Машина с указанным идентификатором не найдена в базе");
            return "errors/404";
        }
        var vehicleDTOZonedDT = vehicleDTOZonedDTOptional.get();
        if (vehicleDTOZonedDT.getEnterpriseId() != enterpriseId) {
            model.addAttribute("message", "Машина с указанным идентификатором не закреплена за данным предприятием");
            return "errors/404";
        }
        //TimeZoneUtility.setClientTimeZone(vehicleDTO, request);
        model.addAttribute("enterpriseId", enterpriseId);
        model.addAttribute("enterpriseName", enterpriseName);
        model.addAttribute("enterprises", enterpriseService.findUserEnterprises(principal.getName()));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("drivers", driverService.findAllByVehicleId(vehicleDTOZonedDT.getId()));
//        model.addAttribute("drivers", driverService.findAllByEnterpriseId(enterpriseId));
        model.addAttribute("vehicle", vehicleDTOZonedDT);
        return "vehicles/one";
    }

    @GetMapping("/vehicle/create")
    public String getCreationPage(Model model, @RequestParam("enterpriseId") int enterpriseId, @RequestParam("enterpriseName") String enterpriseName) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("enterpriseId", enterpriseId);
        model.addAttribute("enterpriseName", enterpriseName);
        return "vehicles/create";
    }

    //@PreAuthorize("hasAuthority('MANAGER')")
    @ResponseBody
    @PostMapping(value = "/api/vehicle/", consumes = "application/json")
    public  ResponseEntity<VehicleDTO> createRest(@RequestBody VehicleCreateDTO vehicleCreateDTO, Principal principal) {
        if (!enterpriseService.checkUserAccessToEnterprise(principal.getName(), vehicleCreateDTO.getEnterpriseId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return vehicleService.saveDTO(vehicleCreateDTO)
                .map(x -> new ResponseEntity<>(x, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PostMapping("/vehicle/create")
    public String create(@ModelAttribute VehicleCreateDTO vehicleCreateDTO, Model model) {
        try {
            Optional<VehicleDTO> savedVehicle = vehicleService.saveDTO(vehicleCreateDTO);
            if (savedVehicle.isEmpty()) {
                model.addAttribute("message", "Машина не зарегистрирована");
                return "errors/404";
            }
            model.addAttribute("message", "Машина зарегистрирована!");
            return "success/success";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @PostMapping("/vehicle/update")
    public String update(@ModelAttribute VehicleUpdateDTO vehicleUpdateDTO, Model model, @RequestParam("driversCount") int driversCount) {
        try {
            var status = vehicleService.update(vehicleUpdateDTO, driversCount);
            if (status == -2) {
                model.addAttribute("message", "Машина с указанным идентификатором не найдена");
                return "errors/404";
            }
            if (status == -3) {
                model.addAttribute("message", "Перемещение машины не возможно: есть назначенные водители");
                return "errors/404";
            }
            if (status == -1) {
                model.addAttribute("message", "Данные по машине обновлены!");
                return "success/success";
            }
            model.addAttribute("message", String.format("Выбранный Вами активный водитель уже назначен машине с id = %d", status));
            return "errors/404";
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
        //забыл почему мы тут беспроблемно вытаскиваем ленивый enterprise))
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

    //удаляет вместе с назначениями и активным водителем
    @GetMapping("/vehicle/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        Optional<Vehicle> v = vehicleService.findById(id);
        if (v.isEmpty()) {
            model.addAttribute("message", "Машина с указанным идентификатором не найдена");
            return "errors/404";
        }
        var isDeleted = vehicleService.deleteByVehicle(v.get());
        if (isDeleted) {
            model.addAttribute("message", "Машина удалена!");
            return "success/success";
        }
            model.addAttribute("message", "Машина найдена, но не удалена");
            return "errors/404";
        //var isDeleted = vehicleService.deleteById(id);
//        if (!isDeleted) {
//            model.addAttribute("message", "Машина с указанным идентификатором не найдена");
//            return "errors/404";
//        }
//        model.addAttribute("message", "Машина удалена!");
//        return "success/success";
    }
}
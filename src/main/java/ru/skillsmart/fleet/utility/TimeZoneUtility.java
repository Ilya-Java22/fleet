package ru.skillsmart.fleet.utility;

import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.dto.VehicleDTOWithZonedDateTime;
import ru.skillsmart.fleet.model.Enterprise;
import ru.skillsmart.fleet.model.Vehicle;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class TimeZoneUtility {
//    public static void setClientTimeZone(VehicleDTO vehicleDTO, HttpServletRequest request) {
//        String clientTimeZone = request.getHeader("Time-Zone");
//        if (clientTimeZone == null) {
//            //сомнительно. Надо бы UTC и предупредить об этом. А лучше, наверное, у зареганых юзеров сделать поле timezone
//            clientTimeZone = TimeZone.getDefault().getID();
//        }
//        var defaultZone = TimeZone.getDefault().toZoneId();
//        LocalDateTime dateTimeInClientTZ = vehicleDTO.getPurchased()
//                .atZone(defaultZone)
//                .withZoneSameInstant(ZoneId.of(clientTimeZone))
//                .toLocalDateTime();
//        vehicleDTO.setPurchased(dateTimeInClientTZ);
//    }

    public static Vehicle setEnterpriseTimeZoneForVehicle(Vehicle vehicle) {
        String enterpriseTimeZone = vehicle.getEnterprise().getTimezone();
        var defaultZone = TimeZone.getDefault().toZoneId();
        LocalDateTime dateTimeInEnterpriseTZ = vehicle.getPurchased()
                .atZone(defaultZone)
                .withZoneSameInstant(ZoneId.of(enterpriseTimeZone))
                .toLocalDateTime();
        vehicle.setPurchased(dateTimeInEnterpriseTZ);
        return vehicle;
    }

    public static VehicleDTOWithZonedDateTime setEnterpriseTZForVehicleDTOWithZonedDateTime(VehicleDTOWithZonedDateTime dto) {
        String enterpriseTimeZone = dto.getEnterpriseTimeZone();
        ZonedDateTime newZonedDateTime = dto.getPurchased().withZoneSameInstant(ZoneId.of(enterpriseTimeZone));
        dto.setPurchased(newZonedDateTime);
        return dto;
    }

    public static LocalDateTime convertTimeZone(LocalDateTime localDateTime, ZoneId initialZoneId, String newTimeZone) {
//        String enterpriseTimeZone = vehicle.getEnterprise().getTimezone();
//        var defaultZone = TimeZone.getDefault().toZoneId();
        return  localDateTime
                .atZone(initialZoneId)
                .withZoneSameInstant(ZoneId.of(newTimeZone))
                .toLocalDateTime();
    }
}

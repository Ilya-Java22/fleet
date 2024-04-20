package ru.skillsmart.fleet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.skillsmart.fleet.service.DataGenerationService;
import ru.skillsmart.fleet.service.EnterpriseService;
import ru.skillsmart.fleet.service.TrackGenerationService;

import javax.annotation.PreDestroy;
import java.util.List;

@ShellComponent
public class TrackGenerationShell {

    @Autowired
    private TrackGenerationService trackGenerationService;


    //можно без key - см. DataGenerationShell
    //в shelloption можно без value - см. DataGenerationShell
    @ShellMethod(key = "generate-track", value = "Generate track for vehicle")
    public String generateTrack(
            @ShellOption(value = {"-v", "--vehicle-id"}, help = "Vehicle ID") Integer vehicleId,
            @ShellOption(value = {"-l", "--track-length"}, help = "track length in meters") Double trackLength,
            @ShellOption(value = {"-p", "--point-coordinates"}, help = "central point coordinates") String centralPoint,
            @ShellOption(value = {"-s", "--track-step"}, help = "track step in meters") Integer trackStep) {

        if (vehicleId == null || trackLength == null || centralPoint == null || trackStep == null) {
            return "Пожалуйста, укажите все необходимые параметры для генерации трека: -v <vehicle_id>,"
                    + " -l <track_length>, -p <point_coordinates>, -s <track_step>";
        }
        trackGenerationService.generateTrack(vehicleId, trackLength, centralPoint, trackStep);
        return "Трек сгенерирован для машины с id = " + vehicleId;
    }

//    @PreDestroy
//    public void destroy() {
//        System.out.println("Track generation stopped");
//    }

}

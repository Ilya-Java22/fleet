package ru.skillsmart.fleet.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillsmart.fleet.dto.VehicleCreateDTO;
import ru.skillsmart.fleet.dto.VehicleDTO;
import ru.skillsmart.fleet.mapper.VehicleMapper;
import ru.skillsmart.fleet.model.Vehicle;
import ru.skillsmart.fleet.repository.EnterpriseRepository;
import ru.skillsmart.fleet.repository.VehicleRepository;

import java.util.*;

@Service
public class SimpleVehicleService implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserService userService;
    private final EnterpriseRepository enterpriseRepository;
    private final VehicleMapper vehicleMapper;

    public SimpleVehicleService(VehicleRepository vehicleRepository, UserService userService,
                                EnterpriseRepository enterpriseRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.userService = userService;
        this.enterpriseRepository = enterpriseRepository;
        this.vehicleMapper = vehicleMapper;
    }

//    заменил на пагинацию, см. ниже
//    @Override
//    public List<VehicleDTO> findAllDto() {
//        return vehicleRepository.findAll().stream()
//                .map(vehicleMapper::getModelFromEntity)
//                .toList();
//    }

    @Override
    public Page<VehicleDTO> findAllDto(Pageable pageable) {
        return vehicleRepository.findAll(pageable)
                .map(vehicleMapper::getModelFromEntity);
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Optional<VehicleDTO> findVehicleDTOById(int id) {
        Optional<Vehicle> vehicle = findById(id);
        return vehicle.isPresent()
                ? vehicle.map(vehicleMapper::getModelFromEntity) : Optional.empty();
    }

    //не нужен, излишне
//    @Override
//    public Optional<Vehicle> findVehicleById(int id) {
//        return vehicleRepository.findVehicleById(id);
//    }

    //используется в SimpleDataGenerationService (не использую там dto)
    @Override
    public Optional<Vehicle> save(Vehicle vehicle) {
        Vehicle resultVehicle = vehicleRepository.save(vehicle);
        return resultVehicle.getId() != 0 ? Optional.of(resultVehicle) : Optional.empty();
    }

    @Override
    public Optional<VehicleDTO> saveDTO(VehicleCreateDTO vehicleCreateDTO) {
        Vehicle vehicle = vehicleMapper.getEntityFromCreateDTOModel(vehicleCreateDTO);
        vehicleRepository.save(vehicle);
        return vehicle.getId() != 0 ? Optional.of(vehicleMapper.getModelFromEntity(vehicle)) : Optional.empty();
    }

    //без тр ошибка - видимо из-за каскада в vehicle

//    @Transactional
//    @Override
//    public boolean deleteById(int id) {
//        return vehicleRepository.deletebyId(id) > 0;
//    }

    //без тр ошибка - видимо из-за каскада в vehicle, а вообще грят, что обязательно, если update delete (видимо тк
    //авторский метод, так как дефолтные в репе по умолчанию транзакционные
    //тут возможно стоит оставить зануление в удаляемых дочках, везде советуют, но рабит так
    @Transactional
    @Override
    public boolean deleteByVehicle(Vehicle vehicle) {
//            vehicle.getAssingingDriversToVehicles().forEach(position -> {
//            position.setVehicle(null);
//            position.setDriver(null);
//        });
        vehicle.getAssingingDriversToVehicles().clear();
        //если закомментить эту строчку, хибер не будет перед созданием временной таблицы удалять assing(!!!) и active
        // а после используя временную удалит актив и пытаясь удалить vehicle, упадет с ошибкой constrains ограничение
        //по внешнему ключу assing(!!!), см ворд в папке проектов Бобровского
        vehicle.setActiveDriver(null);
        return vehicleRepository.deleteByVehicle(vehicle) > 0;
    }

    //возвращает -2, если обновляемая машина не найдена в базе; -1 если успешно обновлено; а если выбранный в качестве
    // активного водитель уже является активным у какой-то машины, то возвращает id этой машины
    @Override
    public Integer update(VehicleDTO vehicleDTO, int driversCount) {
        return vehicleRepository.findById(vehicleDTO.getId())
                .map(x -> {
                    if (vehicleDTO.getEnterpriseId() != x.getEnterprise().getId() && driversCount > 0) {
                        return -3;
                    }
                    Integer vehicleWithThisActiveDriver = vehicleRepository.findByActiveDriverId(vehicleDTO.getActiveDriverId(), vehicleDTO.getId());
                    if (vehicleWithThisActiveDriver != null) {
                        return vehicleWithThisActiveDriver;
                    }
                    vehicleMapper.update(vehicleDTO, x);
                    vehicleRepository.save(x);
                    return -1;
                })
                .orElse(-2);
    }

    @Override
    public void moveVehicleToEnterprise(Vehicle vehicle, Integer newEnterpriseId) {
//        Vehicle vehicle = vehicleRepository.findById(vehicleId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "Vehicle is not found. Please, check id."
//                ));
        vehicle.setEnterprise(enterpriseRepository.getReferenceById(newEnterpriseId));
        vehicleRepository.save(vehicle);
    }

}

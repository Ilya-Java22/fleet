package ru.skillsmart.fleet.service;

import ru.skillsmart.fleet.model.User;

public interface UserService {
    User findUserWithEnterprisesWithDriversVehiclesByUsername(String username);
    User findUserWithEnterprisesByUsername(String username);
}

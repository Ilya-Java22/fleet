package ru.skillsmart.fleet.service;

import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.mapper.EnterpriseMapper;
import ru.skillsmart.fleet.model.User;
import ru.skillsmart.fleet.repository.UserRepository;

@Service
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;
    private final EnterpriseMapper enterpriseMapper;

    public SimpleUserService(UserRepository userRepository, EnterpriseMapper enterpriseMapper) {
        this.userRepository = userRepository;
        this.enterpriseMapper = enterpriseMapper;
    }

    @Override
    public User findUserWithEnterprisesWithDriversVehiclesByUsername(String username) {
        return userRepository.findUserWithEnterprisesWithDriversVehiclesByUsername(username);
    }

    @Override
    public User findUserWithEnterprisesByUsername(String username) {
        return userRepository.findUserWithEnterprisesByUsername(username);
    }
}

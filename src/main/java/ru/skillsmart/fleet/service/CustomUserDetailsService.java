package ru.skillsmart.fleet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skillsmart.fleet.model.User;
import ru.skillsmart.fleet.model.UserDetailsServices;
import ru.skillsmart.fleet.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }
//        UserDetails user = User.builder()
//                .username(myUser.getLogin())
//                .password(myUser.getPassword())
//                .roles(myUser.getRole())
//                .build();
        return new UserDetailsServices(user);
    }
}
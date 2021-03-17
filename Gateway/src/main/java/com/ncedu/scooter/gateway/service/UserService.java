package com.ncedu.scooter.gateway.service;

import com.ncedu.scooter.gateway.entity.Role;
import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.repository.RoleRepository;
import com.ncedu.scooter.gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //по умолчанию при регистрации все юзеры,админы добавляются из базы напрямую
    public boolean saveUser(User user) {
        if (userRepository.findAllByLogin(user.getLogin()) != null) {
            return false;
        } else {
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRole(userRole);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
    }

    public User findByLogin(String login) {
        return userRepository.findAllByLogin(login);
    }

    public User findByLoginAndPassword(String login, String password) {
        User user = findByLogin(login);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}

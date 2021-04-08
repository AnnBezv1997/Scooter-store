package com.ncedu.scooter.gateway.service;

import com.ncedu.scooter.gateway.entity.Role;
import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.exception.PasswordIncorrect;
import com.ncedu.scooter.gateway.exception.SaveFailed;
import com.ncedu.scooter.gateway.exception.UserNotFound;
import com.ncedu.scooter.gateway.repository.RoleRepository;
import com.ncedu.scooter.gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.gateway.exception.ExceptionMessage.ERROR_SAVE;
import static com.ncedu.scooter.gateway.exception.ExceptionMessage.USER_NOT_FOUND;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //по умолчанию при регистрации все юзеры,админы добавляются из базы напрямую
    public boolean saveUser(User user) throws SaveFailed {
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new SaveFailed(ERROR_SAVE);
        } else {
            Role userRole = roleRepository.findByName("ROLE_USER");
            user.setRole(userRole);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        }
    }

    public User findByLogin(String login) throws UserNotFound {

        User user = userRepository.findByLogin(login);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFound(USER_NOT_FOUND);
        }
    }

    public boolean updateLogin(String oldLogin, String newLogin) {
        User u = userRepository.findByLogin(oldLogin);
        u.setLogin(newLogin);
        userRepository.save(u);
        return true;
    }

    public boolean addName(String login, String name) {
        User u = userRepository.findByLogin(login);
        u.setName(name);
        userRepository.save(u);
        return true;
    }

    public User findByLoginAndPassword(String login, String password) throws UserNotFound, PasswordIncorrect {
        User user = userRepository.findByLogin(login);

        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            } else {
                throw new PasswordIncorrect("Password Incorrect");
            }
        } else {
            throw new UserNotFound("User Not Found");
        }
    }

}

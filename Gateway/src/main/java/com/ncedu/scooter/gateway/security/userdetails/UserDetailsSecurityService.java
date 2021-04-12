package com.ncedu.scooter.gateway.security.userdetails;

import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.exception.UserNotFound;
import com.ncedu.scooter.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsSecurityService implements UserDetailsService {
    @Autowired
    private UserService userService;

    //достаю из базы данных моего юзера по логину, конвертирую
    @Override
    public UserDetailsSecurity loadUserByUsername(String username) {
        try {
            User user = userService.findByLogin(username);
            return UserDetailsSecurity.userToUserDetails(user);
        } catch (UserNotFound e) {
            e.getMessage();
            return null;
        }

    }
}

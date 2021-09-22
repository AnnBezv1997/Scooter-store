package com.ncedu.scooter.gateway.controller;

import com.ncedu.scooter.gateway.controller.request.AuthRequest;
import com.ncedu.scooter.gateway.controller.request.AuthResponse;
import com.ncedu.scooter.gateway.controller.request.RegistrationRequest;
import com.ncedu.scooter.gateway.entity.Address;
import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.exception.PasswordIncorrect;
import com.ncedu.scooter.gateway.exception.SaveFailed;
import com.ncedu.scooter.gateway.exception.UserNotFound;
import com.ncedu.scooter.gateway.security.jsonwebtoken.JwtProvider;
import com.ncedu.scooter.gateway.service.AddressService;
import com.ncedu.scooter.gateway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "AuthController", description = "Authentification and registration controller")
@RestController
@RequestMapping(value = "/gateway")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private JwtProvider jwtProvider;

    @Operation(summary = "Registration user", description = "Accepts RegistrationRequest and registers")
    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        try {
            userService.saveUser(u);
            if (registrationRequest.getAddress() != null) {
                Address address = new Address();
                address.setAddress(registrationRequest.getAddress().getAddress());
                address.setUser(u);
                addressService.saveUserAddress(address);
            }
            return "OK";
        } catch (SaveFailed e) {
            e.getMessage();
            return null;
        }
    }

    @Operation(summary = "Authentification user", description = "Authentification user and generates a token")
    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody @Valid AuthRequest request) {
        try {
            User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
            String token = jwtProvider.generateToken(user.getLogin());
            return new AuthResponse(token, user);
        } catch (UserNotFound | PasswordIncorrect ex) {
            ex.getMessage();
            return null;
        }

    }

    @Operation(summary = "Authentification user other microservices", description = "Authentification user")
    @PostMapping("/authmicroservises")
    public User authmicroservises(@RequestBody @Valid String userToken) {
        try {
            User user = new User();
            if (jwtProvider.validateToken(userToken)) {
                String login = jwtProvider.getLoginFromToken(userToken);
                user = userService.findByLogin(login);
            }
            return user;
        } catch (UserNotFound ex) {
            ex.getMessage();
            return null;
        }

    }


}

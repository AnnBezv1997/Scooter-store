package com.ncedu.scooter.gateway.controller;

import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.security.jsonwebtoken.JwtProvider;
import com.ncedu.scooter.gateway.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@Tag(name = "AuthController", description = "Authentification and registration controller")
@RestController
@RequestMapping(value = "/gateway")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @Operation(summary = "Registration user", description = "Accepts RegistrationRequest and registers")
    @PostMapping("/register")
    public boolean registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User u = new User();
        u.setPassword(registrationRequest.getPassword());
        u.setLogin(registrationRequest.getLogin());
        u.setAddress(registrationRequest.getAddress());
        return userService.saveUser(u);
    }

    @Operation(summary = "Authentification user", description = "Authentification user and generates a token")
    @PostMapping("/auth")
    public AuthResponse auth(@RequestBody AuthRequest request) {
        User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(user.getLogin());
        return new AuthResponse(token);
    }


}

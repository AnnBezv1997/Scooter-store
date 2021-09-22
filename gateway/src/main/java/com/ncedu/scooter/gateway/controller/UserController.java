package com.ncedu.scooter.gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.gateway.controller.request.NameAddRequest;
import com.ncedu.scooter.gateway.controller.request.UpdateLoginRequest;
import com.ncedu.scooter.gateway.entity.Address;
import com.ncedu.scooter.gateway.entity.User;
import com.ncedu.scooter.gateway.exception.UserNotFound;
import com.ncedu.scooter.gateway.service.AddressService;
import com.ncedu.scooter.gateway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Tag(name = "UserController", description = "CRUD user")
@RestController
@RequestMapping(value = "/gateway/user")
public class UserController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserService userService;

    @Operation(summary = "Get user", description = "")
    @GetMapping("/{login}")
    public User getUser(@PathVariable(name = "login") String login) throws UserNotFound {
        return userService.findByLogin(login);
    }

    @Operation(summary = "Get address user", description = "")
    @GetMapping("/address/{login}")
    public String getAllAddress(@PathVariable(name = "login") String login) throws JsonProcessingException, UserNotFound {
        ObjectMapper objectMapper = new ObjectMapper();
        User u = userService.findByLogin(login);
        ArrayList<Address> addressList = addressService.findAllUserAddress(u);
        return objectMapper.writeValueAsString(addressList);

    }

    @Operation(summary = "Add user address", description = "")
    @PostMapping("/address/add")
    public Address addUserAddress(@RequestBody @Valid Address addressRequest) throws UserNotFound {
        Address newAddress = new Address();
        User u = userService.findByLogin(addressRequest.getUser().getLogin());
        ArrayList<Address> allAddress = addressService.findAllUserAddress(u);
        for (Address address : allAddress) {
            String newAddr = addressRequest.getAddress().replaceAll("\\.", "")
                    .replaceAll(",", "").replaceAll(" ","").toLowerCase();
            String userAddress = address.getAddress().replaceAll("\\.", "")
                    .replaceAll(",", "").replaceAll(" ","").toLowerCase();
            if (newAddr.equals(userAddress)) {
                return null;
            }
        }
        newAddress.setAddress(addressRequest.getAddress());
        newAddress.setUser(u);
        if (addressService.saveUserAddress(newAddress)) {
            return newAddress;
        } else {
            return null;
        }
    }

    @Operation(summary = "Delete user address", description = "")
    @PostMapping("/address/delete")
    public String deleteAddress(@RequestBody @Valid Address address) {
        if (addressService.deleteAddress(address)) {
            return "OK";
        } else {
            return null;
        }
    }

    @PostMapping("/name/add")
    @Operation(summary = "Add user name.", description = "")
    public User addUserName(@RequestBody @Valid NameAddRequest nameAddRequest) {
        return userService.addName(nameAddRequest.getLogin(), nameAddRequest.getName());

    }

    @PostMapping("/login/update")
    @Operation(summary = "Update user login", description = "")
    public boolean updateUserLogin(@RequestBody @Valid UpdateLoginRequest updateLoginRequest) {
        return userService.updateLogin(updateLoginRequest.getOldLogin(), updateLoginRequest.getNewLogin());

    }
}

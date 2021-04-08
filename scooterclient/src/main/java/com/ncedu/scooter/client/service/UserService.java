package com.ncedu.scooter.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.client.model.Address;
import com.ncedu.scooter.client.model.AddressRequest;
import com.ncedu.scooter.client.model.NameAddRequest;
import com.ncedu.scooter.client.model.UpdateLoginRequest;
import com.ncedu.scooter.client.model.User;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.client.service.Url.USER;
import static com.ncedu.scooter.client.service.Url.DELETE_ADDRESS;
import static com.ncedu.scooter.client.service.Url.ALL_ADDRESS;
import static com.ncedu.scooter.client.service.Url.ADD_USER_NAME;
import static com.ncedu.scooter.client.service.Url.UPDATE_USER_LOGIN;
import static com.ncedu.scooter.client.service.Url.ADD_ADDRESS;

@Service
public class UserService {
    private ScooterRestTemplate scooterRestTemplate;

    public UserService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public String addUserAddress(AddressRequest addressRequest, String token) {
        return scooterRestTemplate.post(ADD_ADDRESS, String.class, addressRequest, token).getBody();
    }

    public String deleteAddress(Address address, String token) {
        return scooterRestTemplate.post(DELETE_ADDRESS, String.class, address, token).getBody();
    }

    public String addUserName(NameAddRequest nameAddRequest, String token) {
        return scooterRestTemplate.post(ADD_USER_NAME, String.class, nameAddRequest, token).getBody();
    }

    public boolean updateUserLogin(UpdateLoginRequest updateLoginRequest, String token) {
        return scooterRestTemplate.post(UPDATE_USER_LOGIN, boolean.class, updateLoginRequest, token).getBody();
    }

    public ArrayList<Address> getAllAddress(String login, String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.get(ALL_ADDRESS + login, String.class, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Address>>() {
        });

    }

    public User getUser(String login, String token) {
        return scooterRestTemplate.get(USER + login, User.class, token).getBody();
    }
}

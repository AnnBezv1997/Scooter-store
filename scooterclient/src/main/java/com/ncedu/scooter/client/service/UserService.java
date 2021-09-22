package com.ncedu.scooter.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.client.model.user.Address;
import com.ncedu.scooter.client.model.request.user.AddressRequest;
import com.ncedu.scooter.client.model.request.user.NameAddRequest;
import com.ncedu.scooter.client.model.request.user.UpdateLoginRequest;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.client.service.Url.*;

@Service
public class UserService {
    private ScooterRestTemplate scooterRestTemplate;

    public UserService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public Address addUserAddress(Address addressRequest, String token) {
        return scooterRestTemplate.post(URL.get("ADD_ADDRESS"), Address.class, addressRequest, token).getBody();
    }

    public String deleteAddress(Address address, String token) {
        return scooterRestTemplate.post(URL.get("DELETE_ADDRESS"), String.class, address, token).getBody();
    }

    public User addUserName(NameAddRequest nameAddRequest, String token) {
        return scooterRestTemplate.post(URL.get("ADD_USER_NAME"), User.class, nameAddRequest, token).getBody();
    }

    public boolean updateUserLogin(UpdateLoginRequest updateLoginRequest, String token) {
        return scooterRestTemplate.post(URL.get("UPDATE_USER_LOGIN"), boolean.class, updateLoginRequest, token).getBody();
    }

    public ArrayList<Address> getAllAddress(String login, String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.get(URL.get("ALL_ADDRESS") + login, String.class, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Address>>() {
        });

    }

    public User getUser(String login, String token) {
        return scooterRestTemplate.get(URL.get("USER") + login, User.class, token).getBody();
    }
}

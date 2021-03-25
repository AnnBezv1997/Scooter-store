package com.ncedu.scooter.client.service;

import com.ncedu.scooter.client.model.RegistrationRequest;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.client.service.Url.REGISTER_URL;

@Service
public class RegistrationService {
    private MyRestTemplate myRestTemplate = new MyRestTemplate();

    public String registration(RegistrationRequest registrationRequest) {

        return myRestTemplate.post(REGISTER_URL, String.class, registrationRequest).getBody();

    }

}

package com.ncedu.scooter.client.service;

import com.ncedu.scooter.client.model.RegistrationRequest;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.client.service.Url.REGISTER_URL;

@Service
public class RegistrationService {

    private ScooterRestTemplate scooterRestTemplate;

    public RegistrationService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public String registration(RegistrationRequest registrationRequest) {

        return scooterRestTemplate.post(REGISTER_URL, String.class, registrationRequest).getBody();

    }

}

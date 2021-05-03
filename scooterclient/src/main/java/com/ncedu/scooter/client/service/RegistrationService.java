package com.ncedu.scooter.client.service;

import com.ncedu.scooter.client.model.request.user.RegistrationRequest;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.client.service.Url.URL;

@Service
public class RegistrationService {

    private ScooterRestTemplate scooterRestTemplate;

    public RegistrationService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public String registration(RegistrationRequest registrationRequest) {

        return scooterRestTemplate.post(URL.get("REGISTER_URL"), String.class, registrationRequest).getBody();

    }

}

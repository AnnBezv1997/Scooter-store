package com.ncedu.scooter.client.service;

import com.ncedu.scooter.client.model.request.user.AuthRequest;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.client.service.Url.URL;


@Service
public class AuthService {

    private ScooterRestTemplate scooterRestTemplate;

    public AuthService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public AuthResponse auth(AuthRequest authRequest) {

        return scooterRestTemplate.post(URL.get("AUTH_URL"), AuthResponse.class, authRequest).getBody();
    }
}

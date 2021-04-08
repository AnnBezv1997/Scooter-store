package com.ncedu.scooter.client.service;

import com.ncedu.scooter.client.model.AuthRequest;
import com.ncedu.scooter.client.model.AuthResponse;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.client.service.Url.AUTH_URL;

@Service
public class AuthService {
    private ScooterRestTemplate scooterRestTemplate;

    public AuthService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public AuthResponse auth(AuthRequest authRequest) {

        return scooterRestTemplate.post(AUTH_URL, AuthResponse.class, authRequest).getBody();
    }
}

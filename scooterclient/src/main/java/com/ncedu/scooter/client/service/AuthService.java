package com.ncedu.scooter.client.service;

import com.ncedu.scooter.client.model.AuthRequest;
import com.ncedu.scooter.client.model.AuthResponse;
import org.springframework.stereotype.Service;

import static com.ncedu.scooter.client.service.Url.AUTH_URL;

@Service
public class AuthService {
    private MyRestTemplate myRestTemplate = new MyRestTemplate();

    public AuthResponse auth(AuthRequest authRequest) {

        return myRestTemplate.post(AUTH_URL, AuthResponse.class, authRequest).getBody();
    }
}

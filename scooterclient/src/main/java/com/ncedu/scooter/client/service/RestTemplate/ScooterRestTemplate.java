package com.ncedu.scooter.client.service.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class ScooterRestTemplate {

    private RestTemplate restTemplate;

    public ScooterRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> post(String url, Class<T> responseType, Object body) {

        HttpEntity<?> requestBody = new HttpEntity<>(body);
        return restTemplate.exchange(url, HttpMethod.POST, requestBody, responseType);
    }

    public <T> ResponseEntity<T> post(String url, Class<T> responseType, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> requestBody = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestBody, responseType);
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> requestBody = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestBody, responseType);
    }
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> requestBody = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.GET, requestBody, responseType);
    }
}

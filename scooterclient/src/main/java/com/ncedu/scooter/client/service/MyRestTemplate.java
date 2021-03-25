package com.ncedu.scooter.client.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class MyRestTemplate {

    private RestTemplate restTemplate = new RestTemplate();

    MyRestTemplate() {

    }

    public <T> ResponseEntity<T> post(String url, Class<T> responseType, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestBody = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestBody, responseType);
    }

}

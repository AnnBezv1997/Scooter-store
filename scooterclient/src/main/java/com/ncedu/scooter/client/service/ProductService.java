package com.ncedu.scooter.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.client.model.Category;
import com.ncedu.scooter.client.model.request.ProductRequest;
import com.ncedu.scooter.client.model.request.ProductResponse;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.client.service.Url.URL;

@Service
public class ProductService {

    private ScooterRestTemplate scooterRestTemplate;

    public ProductService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public ArrayList<Category> getCategories(String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.get(URL.get("CATEGORIES_PRODUCTS"), String.class, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Category>>() {
        });

    }

    public ProductResponse getPageProduct(String token, ProductRequest productRequest) {
        return scooterRestTemplate.post(URL.get("PAGE_PRODUCTS"), ProductResponse.class, productRequest, token).getBody();

    }

}

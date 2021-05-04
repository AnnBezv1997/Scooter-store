package com.ncedu.scooter.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.client.model.order.Basket;
import com.ncedu.scooter.client.model.order.UserOrder;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.model.request.order.ProductRequest;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;


import static com.ncedu.scooter.client.service.Url.URL;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
public class OrderService {
    private ScooterRestTemplate scooterRestTemplate;

    public OrderService(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public ArrayList<Basket> getProductBasket(ProductRequest productRequest, String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.post(URL.get("PRODUCT_BASKET"), String.class, productRequest, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Basket>>() {
        });
    }
    public ArrayList<UserOrder> getUserOrder(Integer userId, String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.post(URL.get("ORDERS"), String.class, userId, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<UserOrder>>() {
        });
    }
    public Product getProduct(Integer id, String token) {
        return scooterRestTemplate.get(URL.get("PRODUCT_ID") + id, Product.class, token).getBody();
    }
    public boolean addProductBasket(Basket basket, String token) {
        return scooterRestTemplate.post(URL.get("ADD_PRODUCT_BASKET"), boolean.class, basket, token).getBody();
    }
    public boolean deleteProductBasket(Basket basket, String token) {
        return scooterRestTemplate.post(URL.get("DELETE_PRODUCT_BASKET"), boolean.class, basket, token).getBody();
    }
    public boolean updateCountProductBasket(Basket basket, String token) {
        return scooterRestTemplate.post(URL.get("UPDATE_COUNT_PRODUCT"), boolean.class, basket, token).getBody();
    }
    public BigDecimal totalPrice(Integer userId, String token) {
        return scooterRestTemplate.get(URL.get("TOTAL_PRICE") + userId, BigDecimal.class, token).getBody();
    }
    public boolean createOrder(UserOrder userOrder, String token) {
        return scooterRestTemplate.post(URL.get("ORDER_CREATE"), boolean.class, userOrder, token).getBody();
    }
    public boolean deleteOrder(ProductRequest productRequest, String token) {
        return scooterRestTemplate.post(URL.get("ORDER_DELETE"), boolean.class, productRequest, token).getBody();
    }
    public boolean updateOrder(UserOrder userOrder, String token) {
        return scooterRestTemplate.post(URL.get("ORDER_UPDATE"), boolean.class, userOrder, token).getBody();
    }
}

package com.ncedu.scooter.client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.client.model.Category;
import com.ncedu.scooter.client.model.Discount;
import com.ncedu.scooter.client.model.Product;
import com.ncedu.scooter.client.model.StockStatus;
import com.ncedu.scooter.client.model.request.ProductRequest;
import com.ncedu.scooter.client.model.request.ProductResponse;
import com.ncedu.scooter.client.service.RestTemplate.ScooterRestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.client.service.Url.URL;

@Service
public class ProductServiceAdmin {
    private ScooterRestTemplate scooterRestTemplate;

    public ProductServiceAdmin(ScooterRestTemplate scooterRestTemplate) {
        this.scooterRestTemplate = scooterRestTemplate;
    }

    public ArrayList<Category> getCategories(String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.get(URL.get("ADMIN_CATEGORIES"), String.class, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Category>>() {
        });

    }

    public ArrayList<Discount> getDiscountList(String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = scooterRestTemplate.get(URL.get("ADMIN_DISCOUNTS"), String.class, token).getBody();
        return objectMapper.readValue(jsonArray, new TypeReference<ArrayList<Discount>>() {
        });

    }

    public ProductResponse getPageProduct(String token, ProductRequest productRequest) {
        return scooterRestTemplate.post(URL.get("ADMIN_PAGE_PRODUCTS"), ProductResponse.class, productRequest, token).getBody();
    }

    public boolean updateProduct(Product product, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_UPDATE_PRODUCT"), boolean.class, product, token).getBody();
    }

    public boolean updateStockStatus(StockStatus stockStatus, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_UPDATE_STOCK_STATUS"), boolean.class, stockStatus, token).getBody();
    }

    public boolean updateDiscount(Discount discount, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_UPDATE_DISCOUNT"), boolean.class, discount, token).getBody();
    }

    public boolean updateCategory(Category category, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_UPDATE_CATEGORY"), boolean.class, category, token).getBody();
    }

    public boolean deleteProduct(Product product, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_DELETE_PRODUCT"), boolean.class, product, token).getBody();
    }

    public boolean deleteCategory(Category category, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_DELETE_CATEGORY"), boolean.class, category, token).getBody();
    }

    public boolean deleteDiscount(Discount discount, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_DELETE_DISCOUNT"), boolean.class, discount, token).getBody();
    }

    public boolean saveProduct(Product product, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_ADD_PRODUCT"), boolean.class, product, token).getBody();
    }

    public boolean saveStockStatus(StockStatus stockStatus, String token) {
        return scooterRestTemplate.post(URL.get("ADMIN_ADD_STOCK_STATUS"), boolean.class, stockStatus, token).getBody();
    }

    public boolean saveDisconut(Discount discount, String token) {

        return scooterRestTemplate.post(URL.get("ADMIN_ADD_DISCOUNT"), boolean.class, discount, token).getBody();
    }

    public boolean saveCategory(Category category, String token) {

        return scooterRestTemplate.post(URL.get("ADMIN_ADD_CATEGORY"), boolean.class, category, token).getBody();
    }

    public boolean saveTest(Object o, String token) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String obj = objectMapper.writeValueAsString(o);
        return scooterRestTemplate.post(URL.get("ADMIN_ADD"), boolean.class, obj, token).getBody();
    }


}

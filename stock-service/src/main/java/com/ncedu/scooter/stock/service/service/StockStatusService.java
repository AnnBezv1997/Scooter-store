package com.ncedu.scooter.stock.service.service;

import com.ncedu.scooter.stock.service.entity.AuthRequest;
import com.ncedu.scooter.stock.service.entity.AuthResponse;
import com.ncedu.scooter.stock.service.entity.StockStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class StockStatusService {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Value("$(admin.login)")
    private String login;
    @Value("$(admin.password)")
    private String password;

    @Scheduled(fixedRate = 900000) //каждые 15 минут отправляет запрос на изменение сток статуса со случайным id товара
    public void updateStockStatus(){
        StockStatus status = new StockStatus();
        status.setId((int)(Math.random()*11));
        status.setCount((int)(Math.random()*300));

        List<ServiceInstance> productInfo = discoveryClient.getInstances("PRODUCT_INFO");
        ServiceInstance serviceInstance = productInfo.get(0);
        String baseUrl = serviceInstance.getUri().toString();

        baseUrl = baseUrl + "/admin/update/status";
        String token = auth().getToken();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestBody = new HttpEntity<>(status,headers);
        boolean b = restTemplate.exchange(baseUrl, HttpMethod.POST, requestBody, boolean.class).getBody();
        System.out.println("Scheduler working: " + status.getId() + " - " + status.getCount());
    }
    private AuthResponse auth(){
        List<ServiceInstance> gatewayAuth = discoveryClient.getInstances("product-zuul-service");
        ServiceInstance serviceInstance = gatewayAuth.get(0);
        String baseUrlGateway = serviceInstance.getUri().toString();

        baseUrlGateway = baseUrlGateway + "/gateway/auth";
        AuthRequest authRequest = new AuthRequest();

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<?> requestBody = new HttpEntity<>(authRequest);
        AuthResponse auth = restTemplate.exchange(baseUrlGateway, HttpMethod.POST, requestBody, AuthResponse.class).getBody();
        return auth;
    }
}

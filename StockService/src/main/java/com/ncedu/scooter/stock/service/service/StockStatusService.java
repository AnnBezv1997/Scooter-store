package com.ncedu.scooter.stock.service.service;

import com.ncedu.scooter.stock.service.entity.StockStatus;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Scheduled(fixedRate = 900000) //каждые 15 минут отправляет запрос на изменение сток статуса со случайным id товара
    public void updateStockStatus(){
        StockStatus status = new StockStatus();
        status.setId((int)(Math.random()*11));
        status.setCount((int)(Math.random()*300));

        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT_INFO");
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();

        baseUrl = baseUrl + "/admin/update/status";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestBody = new HttpEntity<>(status,headers);
       boolean b = restTemplate.exchange(baseUrl, HttpMethod.POST, requestBody, boolean.class).getBody();
        System.out.println("Scheduler working: " + status.getId() + " - " + status.getCount());
    }
}

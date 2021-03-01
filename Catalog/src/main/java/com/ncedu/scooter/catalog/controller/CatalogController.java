package com.ncedu.scooter.catalog.controller;

import com.ncedu.scooter.catalog.modelDTO.Products;
import com.ncedu.scooter.catalog.modelDTO.ProductsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {
    @Autowired
    private DiscoveryClient discoveryClient;
    @GetMapping("/{code}")
    public Products getProducts(@PathVariable(name = "code") int code){
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCTS-ZUUL-SERVICE");
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();

        baseUrl = baseUrl + "/products/code/"+code;
        RestTemplate restTemplate = new RestTemplate();
        Products product = restTemplate.getForObject(baseUrl,Products.class);
        return product;

    }

}

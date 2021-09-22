package com.ncedu.scooter.product.info.security.userdetails;

import com.ncedu.scooter.product.info.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserDetailsSecurityService implements UserDetailsService {
    @Autowired
    private DiscoveryClient discoveryClient;
    //достаю из базы данных моего юзера по логину, конвертирую
    @Override
    public UserDetailsSecurity loadUserByUsername(String token) {

            List<ServiceInstance> instances = discoveryClient.getInstances("product-zuul-service");
            ServiceInstance serviceInstance = instances.get(0);
            String baseUrl = serviceInstance.getUri().toString();

            baseUrl = baseUrl + "/gateway/authmicroservises";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> requestBody = new HttpEntity<>(token,headers);
            User user = restTemplate.exchange(baseUrl, HttpMethod.POST, requestBody, User.class).getBody();

            return UserDetailsSecurity.userToUserDetails(user);

    }
}

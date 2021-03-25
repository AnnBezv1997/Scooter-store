package com.ncedu.scooter.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.vaadin.artur.helpers.LaunchUtil;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * The entry point of the Spring Boot application.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class Application extends SpringBootServletInitializer {


    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

}

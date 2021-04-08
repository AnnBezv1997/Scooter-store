package com.ncedu.scooter.client.service.RestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

@Configuration
public class RestTemplateConfig {
    @Value("${serverUrl}")
    private String serverUrl;
    @Autowired
    RestTemplateBuilder restTemplateBuilder;

    @Bean
    ScooterRestTemplate myRestTemplate() {
        return new ScooterRestTemplate(defaultTemplate());
    }

    @Bean
    RestTemplate defaultTemplate() {
        RestTemplate template = new RestTemplate();
        return setBaseUrl(template);
    }

    private RestTemplate setBaseUrl(RestTemplate template) {
        DefaultUriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();
        uriTemplateHandler.setBaseUrl(serverUrl);
        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().set("Accept", MediaType.APPLICATION_JSON_VALUE);

            return execution.execute(request, body);
        };
        return restTemplateBuilder.uriTemplateHandler(uriTemplateHandler).interceptors(interceptor).build();
    }

}

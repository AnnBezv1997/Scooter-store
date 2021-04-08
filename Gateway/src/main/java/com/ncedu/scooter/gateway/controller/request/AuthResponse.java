package com.ncedu.scooter.gateway.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ncedu.scooter.gateway.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "AuthResponse - token")
public class AuthResponse {
    private String token;
    private User user;
}
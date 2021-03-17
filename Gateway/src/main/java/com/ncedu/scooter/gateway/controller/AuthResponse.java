package com.ncedu.scooter.gateway.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "AuthResponse - token")
class AuthResponse {

    private String token;
}
package com.ncedu.scooter.gateway.controller;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data

class AuthRequest {
    @Schema(description = "Number phone")
    @NotEmpty
    private String login;
    @Schema(description = "password")
    @NotEmpty
    private String password;
}
package com.ncedu.scooter.gateway.controller.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data

public class AuthRequest {
    @Schema(description = "Number phone")
    @NotEmpty
    private String login;
    @Schema(description = "password")
    @NotEmpty
    private String password;
}
package com.ncedu.scooter.gateway.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
@Data
public class UpdateLoginRequest {

    @Schema(description = "Old login")
    @NotEmpty
    private String oldLogin;
    @Schema(description = "New Login")
    @NotEmpty
    private String newLogin;
}

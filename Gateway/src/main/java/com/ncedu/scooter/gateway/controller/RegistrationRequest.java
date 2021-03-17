package com.ncedu.scooter.gateway.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
class RegistrationRequest {
    @Schema(description = "Number phone")
    @NotEmpty

  //  @Pattern(regexp = "^\\+7\\d{10}$")
    private String login;
    @Schema(description = "password")
    @NotEmpty
    private String password;
    @Schema(description = "address")
    @NotEmpty
    private String address;
}
package com.ncedu.scooter.gateway.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddressRequest {

    @Schema(description = "address")
    @NotEmpty
    private String address;
    @Schema(description = "User id")
    @NotEmpty
    private String userLogin;
}

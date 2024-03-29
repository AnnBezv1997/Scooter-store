package com.ncedu.scooter.gateway.controller.request;

import com.ncedu.scooter.gateway.entity.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AddressRequest {

    @Schema(description = "address")
    @NotEmpty
    private Address address;
    @Schema(description = "User id")
    @NotEmpty
    private String userLogin;
}

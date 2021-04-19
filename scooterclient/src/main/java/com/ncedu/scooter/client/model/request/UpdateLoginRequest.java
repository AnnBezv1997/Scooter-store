package com.ncedu.scooter.client.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateLoginRequest {


    @NotEmpty
    @NotNull
    private String oldLogin;
    @NotNull
    @NotEmpty
    private String newLogin;
}
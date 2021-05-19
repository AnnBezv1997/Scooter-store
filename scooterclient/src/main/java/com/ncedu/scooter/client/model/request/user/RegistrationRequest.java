package com.ncedu.scooter.client.model.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @NotEmpty
    private String login;

    @NotEmpty
    private String password;

    private String address;
}
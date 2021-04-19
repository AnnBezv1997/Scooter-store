package com.ncedu.scooter.client.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthRequest {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
}
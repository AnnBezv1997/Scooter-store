package com.ncedu.scooter.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class AuthResponse {
    @JsonCreator
    public AuthResponse(@JsonProperty("token") String token) {
        this.token = token;
    }
    private String token;
}

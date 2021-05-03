package com.ncedu.scooter.client.model.request.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncedu.scooter.client.model.user.User;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class AuthResponse {
    @JsonCreator
    public AuthResponse(@JsonProperty("token") String token, @JsonProperty("user") User user) {
        this.token = token;
        this.user = user;
    }
    private String token;
    private User user;
}

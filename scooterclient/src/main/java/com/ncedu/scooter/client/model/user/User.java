package com.ncedu.scooter.client.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncedu.scooter.client.model.user.Role;
import lombok.Data;


@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonCreator
    public User(@JsonProperty("id") Integer id, @JsonProperty("login") String login,
                @JsonProperty("password") String password, @JsonProperty("name") String name,
                @JsonProperty("role") Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    private Integer id;
    private String login;
    private String password;
    private String name;
    private Role role;


}
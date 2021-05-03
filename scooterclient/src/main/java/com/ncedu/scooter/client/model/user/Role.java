package com.ncedu.scooter.client.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    @JsonCreator
    public Role(@JsonProperty("id") Integer id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
    private Integer id;
    private String name;
}

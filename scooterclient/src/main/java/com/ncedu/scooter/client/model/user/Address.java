package com.ncedu.scooter.client.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor

public class  Address {

    @JsonCreator
    public Address(@JsonProperty("id") Integer id, @JsonProperty("address") String address, @JsonProperty("user") User user) {
        this.id = id;
        this.address = address;
        this.user = user;
    }

    private Integer id;

    private String address;

    private User user;

}

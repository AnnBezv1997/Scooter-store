package com.ncedu.scooter.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponse {
    private ArrayList<Address> addressArrayList;

    public AddressResponse(@JsonProperty("addressArrayList") ArrayList<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }
}

package com.ncedu.scooter.gateway.controller.request;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ncedu.scooter.gateway.entity.Address;
import lombok.Data;

import java.util.ArrayList;

@Data

public class AddressResponse {
    private ArrayList<Address> addressArrayList;

    public AddressResponse(ArrayList<Address> addressArrayList) {
        this.addressArrayList = addressArrayList;
    }
}

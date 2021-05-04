package com.ncedu.scooter.client.model.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum DiscountType {

    PERCENT,
    ABSOLUTE

}



package com.ncedu.scooter.order.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class PriceResponse {
    @JsonCreator
    public PriceResponse(@JsonProperty("totalPrice") BigDecimal totalPrice, @JsonProperty("totalDiscount") BigDecimal totalDiscount) {
        this.totalPrice = totalPrice;
        this.totalDiscount = totalDiscount;
    }

    BigDecimal totalPrice;
    BigDecimal totalDiscount;

}

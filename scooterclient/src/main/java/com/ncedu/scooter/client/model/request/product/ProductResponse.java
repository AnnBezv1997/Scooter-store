package com.ncedu.scooter.client.model.request.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ncedu.scooter.client.model.product.Product;
import lombok.Data;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductResponse {
    @JsonCreator
    public ProductResponse(@JsonProperty("totalPages")int totalPages,
                           @JsonProperty("productList") List<Product> productList) {

        this.totalPages = totalPages;
        this.productList = productList;
    }
    private int totalPages;
    private List<Product> productList;

}

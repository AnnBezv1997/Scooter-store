package com.ncedu.scooter.client.model.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Product {
    @JsonCreator
    public Product(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
                   @JsonProperty("description") String description, @JsonProperty("price") BigDecimal price,
                   @JsonProperty("image") String image, @JsonProperty("stockStatus") StockStatus stockStatus,
                   @JsonProperty("category") Category category, @JsonProperty("discount") Discount discount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.stockStatus = stockStatus;
        this.category = category;
        this.discount = discount;
    }

    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private BigDecimal price;
    private String image;
    private StockStatus stockStatus;
    private Category category;
    private Discount discount;
}

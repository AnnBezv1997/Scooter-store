package com.ncedu.scooter.client.model.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Basket {
    @JsonCreator
    public Basket(@JsonProperty("id") Integer id, @JsonProperty("userId") Integer userId,
                  @JsonProperty("userOrder") UserOrder userOrder, @JsonProperty("productId") Integer productId,
                  @JsonProperty("price") BigDecimal price, @JsonProperty("countProduct") Integer countProduct,
                  @JsonProperty("date") Date date) {
        this.id = id;
        this.userId = userId;
        this.userOrder = userOrder;
        this.productId = productId;
        this.price = price;
        this.countProduct = countProduct;
        this.date = date;
    }

    private Integer id;
    @NotNull
    private Integer userId;
    private UserOrder userOrder;
    private Integer productId;
    private BigDecimal price;
    private Integer countProduct;
    private Date date;
}

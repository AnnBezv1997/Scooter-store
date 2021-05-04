package com.ncedu.scooter.client.model.order;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class UserOrder {
    @JsonCreator
    public UserOrder(@JsonProperty("id") Integer id, @JsonProperty("userId") Integer userId,
                     @JsonProperty("address") String address, @JsonProperty("date") Date date,
                     @JsonProperty("totalPrice") BigDecimal totalPrice,
                     @JsonProperty("orderStatusPay") OrderStatusPay orderStatusPay,
                     @JsonProperty("orderStatus") OrderStatus orderStatus) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.date = date;
        this.totalPrice = totalPrice;
        this.orderStatusPay = orderStatusPay;
        this.orderStatus = orderStatus;
    }


    private Integer id;
    @NotNull
    private Integer userId;
    @NotEmpty
    private String address;
    private Date date;
    private BigDecimal totalPrice;
    private OrderStatusPay orderStatusPay;
    private OrderStatus orderStatus;

}
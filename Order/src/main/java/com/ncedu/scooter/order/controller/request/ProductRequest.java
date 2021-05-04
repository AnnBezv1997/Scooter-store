package com.ncedu.scooter.order.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Integer orderId;
    @NotNull
    private Integer userId;
}

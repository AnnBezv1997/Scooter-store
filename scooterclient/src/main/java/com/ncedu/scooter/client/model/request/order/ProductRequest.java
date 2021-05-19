package com.ncedu.scooter.client.model.request.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor

public class ProductRequest {

    private Integer orderId;
    @NotNull
    private Integer userId;
}

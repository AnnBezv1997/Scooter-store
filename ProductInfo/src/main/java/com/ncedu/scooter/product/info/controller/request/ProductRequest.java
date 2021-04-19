package com.ncedu.scooter.product.info.controller.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor

public class ProductRequest {
    @NotNull
    private Integer numberPage;
    @NotNull
    private Integer size;
    private String sortBy;
    private String sortDirection;
    private Integer categorySort;
    private String search;



}

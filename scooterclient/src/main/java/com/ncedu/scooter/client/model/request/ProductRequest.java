package com.ncedu.scooter.client.model.request;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"numderPage", "size", "sortBy", "sortDirection", "categorySort", "search"})
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

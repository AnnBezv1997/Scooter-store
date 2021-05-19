package com.ncedu.scooter.stock.service.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class StockStatus {
    @JsonCreator
    public StockStatus(@JsonProperty("id") int id, @JsonProperty("count") int count) {
        this.id = id;
        this.count = count;

    }
    @NotNull
    private int id;
    @NotNull
    private int count;

}

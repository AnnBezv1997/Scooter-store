package com.ncedu.scooter.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Discount {
    @JsonCreator
    public Discount(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
                    @JsonProperty("description") String description,
                    @JsonProperty("discountType") DiscountType discountType,
                    @JsonProperty("value") BigDecimal value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.discountType = discountType;
        this.value = value;
    }

    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    private DiscountType discountType;
    private BigDecimal value;
}

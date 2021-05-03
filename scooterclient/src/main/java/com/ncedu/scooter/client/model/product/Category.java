package com.ncedu.scooter.client.model.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Category {
    @JsonCreator
    public Category(@JsonProperty("id") Integer id, @JsonProperty("name") String name,
                    @JsonProperty("description") String description,
                    @JsonProperty("categoryParent") Category categoryParent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryParent = categoryParent;
    }

    private Integer id;
    private String name;
    private String description;
    private Category categoryParent;
}

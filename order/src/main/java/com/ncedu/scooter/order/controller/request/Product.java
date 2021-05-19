package com.ncedu.scooter.order.controller.request;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "description", "price", "image", "stockStatus", "category", "discount"})

public class Product {

    @Column(name = "id")
    @Id
    @SequenceGenerator(name = "productIdSeq", sequenceName = "product_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productIdSeq")
    @Schema(description = "Id")
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "price")
    @NotNull
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    @OneToOne(cascade = CascadeType.ALL)
  //@PrimaryKeyJoinColumn
    private StockStatus stockStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}

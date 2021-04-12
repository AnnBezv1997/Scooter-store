package com.ncedu.scooter.product.info.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Schema(description = "Entity Product")
@Entity(name = "product")
@Table(name = "product")
@Data
@AllArgsConstructor
public class Product {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Id")
    @NotEmpty
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    private String name;
    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "price")
    @NotEmpty
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    @NotEmpty
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StockStatus stockStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
}

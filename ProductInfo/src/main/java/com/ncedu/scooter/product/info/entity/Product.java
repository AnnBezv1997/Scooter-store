package com.ncedu.scooter.product.info.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Schema(description = "Entity Product")
@Entity(name = "product")
@Table(name = "product")
@Data
public class Product {

    @Column(name = "code")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Code")
    @NotEmpty
    private int code;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "price")
    @NotEmpty
    private double price;

    @Column(name = "category")
    @NotEmpty
    private String category;

    @Column(name = "description")
    @NotEmpty
    private String description;

    @Column(name = "manufacturer")
    @NotEmpty
    private String manufacturer;

}

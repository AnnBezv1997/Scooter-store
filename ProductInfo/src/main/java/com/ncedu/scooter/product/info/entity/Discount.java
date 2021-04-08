package com.ncedu.scooter.product.info.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotEmpty;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import java.math.BigDecimal;

@Schema(description = "Entity Discount")
@Entity(name = "discount")
@Table(name = "discount")
@Data
@AllArgsConstructor
public class Discount {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private  DiscountType discountType;

    @Column(name = "value")
    private BigDecimal value;

}
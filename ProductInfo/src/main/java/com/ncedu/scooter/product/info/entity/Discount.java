package com.ncedu.scooter.product.info.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Schema(description = "Entity Discount")
@Entity(name = "discount")
@Table(name = "discount")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Discount {

    @Column(name = "id")
    @Id
    @SequenceGenerator(name = "discountIdSeq", sequenceName = "discount_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discountIdSeq")
    @Schema(description = "Id")
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "description")
    @NotEmpty
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Type(type = "com.ncedu.scooter.product.info.service.EnumTypePostgreSql")
    private DiscountType discountType;

    @Column(name = "value")
    private BigDecimal value;

}
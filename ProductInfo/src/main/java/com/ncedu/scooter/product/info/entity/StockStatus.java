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

@Schema(description = "Entity StockStatus")
@Entity(name = "stock_status")
@Table(name = "stock_status")
@Data
@AllArgsConstructor
public class StockStatus {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Id")
    @NotEmpty
    private int id;

    @Column(name = "count")
    private int count;

}
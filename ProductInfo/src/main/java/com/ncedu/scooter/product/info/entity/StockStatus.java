package com.ncedu.scooter.product.info.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Schema(description = "Entity StockStatus")
@Entity(name = "stock_status")
@Table(name = "stock_status")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StockStatus {

    @Column(name = "id")
    @Id
    @SequenceGenerator(name = "stockStatusIdSeq", sequenceName = "stock_status_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockStatusIdSeq")
    @Schema(description = "Id")
    @NotNull
    private int id;

    @Column(name = "count")
    private int count;

}
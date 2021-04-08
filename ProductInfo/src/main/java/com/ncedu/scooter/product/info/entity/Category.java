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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Schema(description = "Entity Category")
@Entity(name = "category")
@Table(name = "category")
@Data
@AllArgsConstructor
public class Category {

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

   @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category categoryParent;

}
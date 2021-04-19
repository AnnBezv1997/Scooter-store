package com.ncedu.scooter.product.info.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Schema(description = "Entity Category")
@Entity(name = "category")
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "name", "description", "parent_id"})

public class Category {

    @Column(name = "id")
    @Id
    @SequenceGenerator(name = "categoryIdSeq", sequenceName = "category_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoryIdSeq")
    @Schema(description = "Id")

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
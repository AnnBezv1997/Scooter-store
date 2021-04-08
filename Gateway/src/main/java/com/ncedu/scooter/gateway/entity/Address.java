package com.ncedu.scooter.gateway.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
@Schema(description = "Entity user address")
@Entity(name = "address")
@Table(name = "address")
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id")
    private Integer id;

    @Column
    private String address;
    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private User user;
}

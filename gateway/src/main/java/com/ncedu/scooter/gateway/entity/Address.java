package com.ncedu.scooter.gateway.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Schema(description = "Entity user address")
@Entity(name = "address")
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
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

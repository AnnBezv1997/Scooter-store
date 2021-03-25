package com.ncedu.scooter.gateway.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Schema(description = "Entity user account")
@Entity(name = "user")
@Table(name = "user_account")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id")
    private Integer id;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}

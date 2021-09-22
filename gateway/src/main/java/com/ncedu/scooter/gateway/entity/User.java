package com.ncedu.scooter.gateway.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Schema(description = "Entity user account")
@Entity(name = "user")
@Table(name = "user_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(name = "userAccountIdSeq", sequenceName = "user_account_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userAccountIdSeq")
    @Schema(description = "Id")
    private Integer id;

    @Column
    private String login;

    @Column
    private String password;
    @Column
    private String name = "";

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


}

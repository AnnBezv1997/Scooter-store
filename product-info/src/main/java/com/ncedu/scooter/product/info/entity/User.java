package com.ncedu.scooter.product.info.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;
    private String login;
    private String password;
    private String name = "";
    private Role role;


}

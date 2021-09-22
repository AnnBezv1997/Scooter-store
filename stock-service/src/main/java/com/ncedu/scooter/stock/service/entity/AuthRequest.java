package com.ncedu.scooter.stock.service.entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {

    private String login;

    public AuthRequest() {
        this.login = "79999999999";
        this.password = "admin";
    }

    private String password ;
}
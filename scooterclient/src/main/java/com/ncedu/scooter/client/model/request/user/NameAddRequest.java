package com.ncedu.scooter.client.model.request.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data

@AllArgsConstructor
public class NameAddRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String login;

}
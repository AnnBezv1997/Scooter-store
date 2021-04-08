package com.ncedu.scooter.gateway.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class NameAddRequest {

    @Schema(description = "Name")
    @NotEmpty
    private String name;
    @Schema(description = "Id User")
    @NotEmpty
    private String login;

}
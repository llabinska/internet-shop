package com.internet.shop.dto.login;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequestDto {

    @NotBlank(message = "{name.not.empty}")
    private String username;

    @NotBlank(message = "{password.not.empty}")
    private String password;

}

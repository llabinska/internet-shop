package com.internet.shop.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.internet.shop.dto.account.AccountResponseDto;
import lombok.Data;

@Data
public class UserResponseDto {

    private long id;

    private String userName;

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AccountResponseDto account;

}

package com.internet.shop.dto.user;

import com.internet.shop.dto.account.AccountResponseDto;
import lombok.Data;

@Data
public class UserResponseDto {

    private long id;

    private String name;

    private String email;

    private AccountResponseDto account;

}

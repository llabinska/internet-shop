package com.internet.shop.dto.login;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginResponseDto {

    private final String token;

}

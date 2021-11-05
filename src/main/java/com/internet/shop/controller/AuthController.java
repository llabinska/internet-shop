package com.internet.shop.controller;

import com.internet.shop.dto.login.LoginRequestDto;
import com.internet.shop.dto.login.LoginResponseDto;
import com.internet.shop.dto.user.UserRequestDto;
import com.internet.shop.dto.user.UserResponseDto;
import com.internet.shop.security.JwtProvider;
import com.internet.shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtProvider jwtProvider;


    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.create(userRequestDto));
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        UserResponseDto userResponseDto = userService.findByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        String token = jwtProvider.generateToken(userResponseDto.getUserName());
        return new LoginResponseDto(token);
    }

}

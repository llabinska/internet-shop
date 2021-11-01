package com.internet.shop.dto.error;

import lombok.Data;

import java.util.List;

@Data
public class GeneralErrorDto {

    private final String message;

    private final List<String> errors;

}

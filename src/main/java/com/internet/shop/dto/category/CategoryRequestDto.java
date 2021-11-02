package com.internet.shop.dto.category;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "{name.not.empty}")
    private String name;

}

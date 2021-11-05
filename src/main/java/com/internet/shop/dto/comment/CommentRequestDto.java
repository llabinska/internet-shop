package com.internet.shop.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentRequestDto {

    @NotBlank
    private String message;

}

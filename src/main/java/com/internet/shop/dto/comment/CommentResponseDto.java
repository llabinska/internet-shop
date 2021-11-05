package com.internet.shop.dto.comment;

import lombok.Data;

@Data
public class CommentResponseDto {

    private Long id;
    private String message;
    private Long userId;
    private Long productId;

}

package com.internet.shop.mapper;

import com.internet.shop.dmo.Comment;
import com.internet.shop.dto.comment.CommentResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setMessage(comment.getMessage());
        commentResponseDto.setProductId(comment.getProduct().getId());
        commentResponseDto.setUserId(comment.getUser().getId());
        return commentResponseDto;
    }

}

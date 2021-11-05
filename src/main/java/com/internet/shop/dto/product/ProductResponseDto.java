package com.internet.shop.dto.product;

import com.internet.shop.dto.category.CategoryResponseDto;
import com.internet.shop.dto.comment.CommentResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponseDto {

    private long id;
    private String name;
    private double price;
    private CategoryResponseDto category;
    private List<CommentResponseDto> comments;

}

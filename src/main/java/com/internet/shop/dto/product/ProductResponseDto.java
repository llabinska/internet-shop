package com.internet.shop.dto.product;

import com.internet.shop.dto.category.CategoryResponseDto;
import lombok.Data;

@Data
public class ProductResponseDto {

    private long id;
    private String name;
    private double price;
    private CategoryResponseDto category;

}

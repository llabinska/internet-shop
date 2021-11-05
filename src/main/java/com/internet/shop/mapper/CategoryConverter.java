package com.internet.shop.mapper;

import com.internet.shop.dmo.Category;
import com.internet.shop.dto.category.CategoryResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryResponseDto toCategoryResponseDto(Category category) {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());
        return categoryResponseDto;
    }

}

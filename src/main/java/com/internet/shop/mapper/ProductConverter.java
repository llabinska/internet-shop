package com.internet.shop.mapper;

import com.internet.shop.dmo.Product;
import com.internet.shop.dto.comment.CommentResponseDto;
import com.internet.shop.dto.product.ProductResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ProductConverter {

    private final CategoryConverter categoryConverter;
    private final CommentConverter commentConverter;

    public ProductResponseDto toProductResponseDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCategory(categoryConverter.toCategoryResponseDto(product.getCategory()));
        List<CommentResponseDto> comments = product.getComments().stream()
                .map(commentConverter::toCommentResponseDto)
                .collect(Collectors.toList());
        productResponseDto.setComments(comments);
        return productResponseDto;
    }

}

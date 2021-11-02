package com.internet.shop.service;

import com.internet.shop.dmo.Category;
import com.internet.shop.dmo.Product;
import com.internet.shop.dto.product.ProductRequestDto;
import com.internet.shop.dto.product.ProductResponseDto;
import com.internet.shop.exception.CategoryNotFoundException;
import com.internet.shop.exception.ProductNotFoundException;
import com.internet.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private static final String PRODUCT_NOT_FOUND_MSG = "Product with id = %s not found";

    private final ProductRepository productRepository;

    private final CategoryService categoryService;


    public Page<ProductResponseDto> search(Pageable pageable, String categoryName, String productName, Double minPrice, Double maxPrice) {

        if (StringUtils.hasText(categoryName) && !StringUtils.hasText(productName)) {
            return getByCategoryNameAndPriceRange(pageable, categoryName, minPrice, maxPrice);
        }

        if (!StringUtils.hasText(categoryName) && StringUtils.hasText(productName)) {
            return getByNameLikeAndPriceRange(pageable, productName, minPrice, maxPrice);
        }

        if (StringUtils.hasText(categoryName) && StringUtils.hasText(productName)) {
            return getByNameLikeAndCategoryNameAndPriceRange(pageable, productName, categoryName, minPrice, maxPrice);
        }

        return getByPriceRange(pageable, minPrice, maxPrice);

    }

    public ProductResponseDto getById(long id) throws CategoryNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MSG, id)));
        return toProductResponseDto(product);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MSG, id)));
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        return toProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());

        Category category = categoryService.getCategoryByName(productRequestDto.getCategory().getName());
        product.setCategory(category);

        return toProductResponseDto(product);
    }

    private ProductResponseDto toProductResponseDto(Product product) {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(product.getId());
        productResponseDto.setName(product.getName());
        productResponseDto.setPrice(product.getPrice());
        productResponseDto.setCategory(categoryService.toCategoryResponseDto(product.getCategory()));
        return productResponseDto;
    }

    private Page<ProductResponseDto> getByPriceRange(Pageable pageable, Double minPrice, Double maxPrice) {
        return productRepository.findAllByPriceBetween(minPrice, maxPrice, pageable).map(this::toProductResponseDto);
    }

    private Page<ProductResponseDto> getByCategoryNameAndPriceRange(Pageable pageable, String categoryName, Double minPrice, Double maxPrice) throws CategoryNotFoundException {
        categoryService.getCategoryByName(categoryName);
        return productRepository.findByCategory_NameAndPriceBetween(categoryName, minPrice, maxPrice, pageable).map(this::toProductResponseDto);
    }

    private Page<ProductResponseDto> getByNameLikeAndPriceRange(Pageable pageable, String name, Double minPrice, Double maxPrice) throws CategoryNotFoundException {
        return productRepository.findByNameLikeIgnoreCaseAndPriceBetween(name, minPrice, maxPrice, pageable).map(this::toProductResponseDto);
    }

    private Page<ProductResponseDto> getByNameLikeAndCategoryNameAndPriceRange(Pageable pageable, String name, String categoryName, Double minPrice, Double maxPrice) throws CategoryNotFoundException {
        return productRepository.findByNameLikeIgnoreCaseAndCategory_NameAndPriceBetween(name, categoryName, minPrice, maxPrice, pageable).map(this::toProductResponseDto);
    }

}

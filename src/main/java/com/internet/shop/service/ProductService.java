package com.internet.shop.service;

import com.internet.shop.dmo.Category;
import com.internet.shop.dmo.Product;
import com.internet.shop.dto.product.ProductRequestDto;
import com.internet.shop.dto.product.ProductResponseDto;
import com.internet.shop.exception.CategoryNotFoundException;
import com.internet.shop.exception.ProductNotFoundException;
import com.internet.shop.mapper.ProductConverter;
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

    private final ProductConverter productConverter;

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
        Product product = getProductById(id);
        return productConverter.toProductResponseDto(product);
    }

    @Transactional
    public void deleteById(Long id) {
        getProductById(id);
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto productRequestDto) {
        Product product = getProductById(id);
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());
        productRepository.save(product);
        return productConverter.toProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto create(ProductRequestDto productRequestDto) {
        Product product = new Product();
        product.setName(productRequestDto.getName());
        product.setPrice(productRequestDto.getPrice());

        Category category = categoryService.getCategoryByName(productRequestDto.getCategory().getName());
        product.setCategory(category);

        Product createdProduct = productRepository.save(product);
        return productConverter.toProductResponseDto(createdProduct);
    }

    Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MSG, id)));
    }

    private Page<ProductResponseDto> getByPriceRange(Pageable pageable, Double minPrice, Double maxPrice) {
        return productRepository.findAllByPriceBetween(minPrice, maxPrice, pageable).map(productConverter::toProductResponseDto);
    }

    private Page<ProductResponseDto> getByCategoryNameAndPriceRange(Pageable pageable, String categoryName, Double minPrice, Double maxPrice) throws CategoryNotFoundException {
        categoryService.getCategoryByName(categoryName);
        return productRepository.findByCategory_NameAndPriceBetween(categoryName, minPrice, maxPrice, pageable).map(productConverter::toProductResponseDto);
    }

    private Page<ProductResponseDto> getByNameLikeAndPriceRange(Pageable pageable, String productName, Double minPrice, Double maxPrice) throws CategoryNotFoundException {
        return productRepository.findByNameContainingIgnoreCaseAndPriceBetween(productName, minPrice, maxPrice, pageable).map(productConverter::toProductResponseDto);
    }

    private Page<ProductResponseDto> getByNameLikeAndCategoryNameAndPriceRange(Pageable pageable, String productName, String categoryName, Double minPrice, Double maxPrice) throws CategoryNotFoundException {
        return productRepository.findByNameContainingIgnoreCaseAndCategory_NameAndPriceBetween(productName, categoryName, minPrice, maxPrice, pageable).map(productConverter::toProductResponseDto);
    }

}

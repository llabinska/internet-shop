package com.internet.shop.controller;

import com.internet.shop.dto.product.ProductRequestDto;
import com.internet.shop.dto.product.ProductResponseDto;
import com.internet.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.create(productRequestDto));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponseDto>> search(@RequestParam(value = "productName", required = false) String productName,
                                                           @RequestParam(value = "categoryName", required = false) String categoryName,
                                                           @RequestParam(value = "minPrice", required = false, defaultValue = "0.0") Double minPrice,
                                                           @RequestParam(value = "maxPrice", required = false, defaultValue = "1000000.0") Double maxPrice,
                                                           @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.search(paging, categoryName, productName, minPrice, maxPrice));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable("productId") Long productId, @RequestBody @Valid ProductRequestDto productRequestDto) {
        return ResponseEntity.ok(productService.update(productId, productRequestDto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> delete(@PathVariable("productId") Long productId) {
        productService.deleteById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

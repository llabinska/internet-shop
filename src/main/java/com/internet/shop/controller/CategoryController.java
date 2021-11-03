package com.internet.shop.controller;

import com.internet.shop.dto.category.CategoryRequestDto;
import com.internet.shop.dto.category.CategoryResponseDto;
import com.internet.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(categoryService.createIfNotExists(categoryRequestDto));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAll(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.ok(categoryService.getAll(paging));
    }

    @GetMapping("/name/{categoryName}")
    public ResponseEntity<CategoryResponseDto> getByName(@PathVariable("categoryName") String categoryName) {
        return ResponseEntity.ok(categoryService.getByName(categoryName));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> getById(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(categoryService.getById(categoryId));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable("categoryId") Long categoryId, @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(categoryService.update(categoryId, categoryRequestDto));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> delete(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

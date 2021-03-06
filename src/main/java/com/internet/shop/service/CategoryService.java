package com.internet.shop.service;

import com.internet.shop.dmo.Category;
import com.internet.shop.dto.category.CategoryRequestDto;
import com.internet.shop.dto.category.CategoryResponseDto;
import com.internet.shop.exception.CategoryAlreadyExistsException;
import com.internet.shop.exception.CategoryNotFoundException;
import com.internet.shop.mapper.CategoryConverter;
import com.internet.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private static final String CATEGORY_NOT_FOUND_ID_MSG = "Category with id = %s not found";
    private static final String CATEGORY_NOT_FOUND_NAME_MSG = "Category with name = %s not found";
    public static final String CATEGORY_WITH_NAME_ALREADY_EXISTS_MSG = "Category with name=%s already exists!";

    private final CategoryRepository categoryRepository;

    private final CategoryConverter categoryConverter;

    @Transactional
    public CategoryResponseDto createIfNotExists(CategoryRequestDto categoryRequestDto) {
        String name = categoryRequestDto.getName();
        Category category = categoryRepository.getByName(name);

        if (category != null) {
            throw new CategoryAlreadyExistsException(String.format(CATEGORY_WITH_NAME_ALREADY_EXISTS_MSG, name));
        }

        category = new Category();
        category.setName(name);

        Category createdCategory = categoryRepository.save(category);
        return categoryConverter.toCategoryResponseDto(createdCategory);
    }

    @Transactional
    public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = getCategoryById(id);

        String name = categoryRequestDto.getName();
        Category categoryByName = categoryRepository.getByName(name);
        if (categoryByName != null) {
            throw new CategoryAlreadyExistsException(String.format(CATEGORY_WITH_NAME_ALREADY_EXISTS_MSG, name));
        }

        category.setName(name);
        Category updatedCategory = categoryRepository.save(category);
        return categoryConverter.toCategoryResponseDto(updatedCategory);
    }

    @Transactional
    public void deleteById(Long id) {
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }

    public CategoryResponseDto getByName(String name) {
        Category category = getCategoryByName(name);
        return categoryConverter.toCategoryResponseDto(category);
    }

    Category getCategoryByName(String name) {
        Category category = categoryRepository.getByName(name);
        if (category == null) {
            throw new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_NAME_MSG, name));
        }
        return category;
    }

    public CategoryResponseDto getById(Long id) {
        Category category = getCategoryById(id);
        return categoryConverter.toCategoryResponseDto(category);
    }

    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryConverter::toCategoryResponseDto);
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(String.format(CATEGORY_NOT_FOUND_ID_MSG, id)));
    }

}

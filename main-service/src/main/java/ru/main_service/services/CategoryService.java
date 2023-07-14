package ru.main_service.services;

import ru.main_service.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long id);

    CategoryDto updateCategory(Long catId, CategoryDto updateDto);

    CategoryDto createCategory(CategoryDto newDto);

    void deleteCategory(Long id);

}


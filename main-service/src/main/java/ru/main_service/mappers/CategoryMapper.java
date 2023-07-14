package ru.main_service.mappers;

import lombok.extern.slf4j.Slf4j;
import ru.main_service.model.Category;
import ru.main_service.model.dto.CategoryDto;

@Slf4j
public class CategoryMapper {

    public static CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        if (category.getId() != null) categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category mapToModel(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

}


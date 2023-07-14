package ru.main_service.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.main_service.exceptions.NotFoundException;
import ru.main_service.mappers.CategoryMapper;
import ru.main_service.model.Category;
import ru.main_service.model.dto.CategoryDto;
import ru.main_service.repositories.CategoryRepository;
import ru.main_service.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto newDto) {
        Category category = categoryRepository.save(CategoryMapper.mapToModel(newDto));
        return CategoryMapper.mapToDto(category);
    }

    @Transactional
    @Override
    public CategoryDto getCategory(Long categoryId) {
        return CategoryMapper.mapToDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id- " + categoryId + "не найдена")));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from, size);
        List<Category> categories = categoryRepository.findAll(page).getContent();
        return categories.stream()
                .map(CategoryMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto updateDto) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Категория с id-" + updateDto.getId() + " не найдена");
        }
        Category category = categoryRepository.getReferenceById(catId);
        category.setName(updateDto.getName());
        Category updateCategory = categoryRepository.save(category);
        return CategoryMapper.mapToDto(updateCategory);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new NotFoundException("Категория с id-" + categoryId + " не найдена");
        }
        categoryRepository.deleteById(categoryId);
    }
}

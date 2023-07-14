package ru.practicum.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.model.dto.CategoryDto;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.services.CategoryService;

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


package ru.main_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.main_service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}


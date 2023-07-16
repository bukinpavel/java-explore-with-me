package ru.main_service.repositories;

import org.springframework.data.domain.Pageable;
import ru.main_service.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}

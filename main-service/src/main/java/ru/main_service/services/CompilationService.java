package ru.main_service.services;

import ru.main_service.model.dto.CompilationDto;
import ru.main_service.model.dto.CompilationNewDto;
import ru.main_service.model.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long id);

    CompilationDto createCompilation(CompilationNewDto newDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto updateDto);



}

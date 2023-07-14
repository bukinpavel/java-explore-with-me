package ru.practicum.services;

import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.CompilationNewDto;
import ru.practicum.model.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long id);

    CompilationDto createCompilation(CompilationNewDto newDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, CompilationUpdateDto updateDto);



}


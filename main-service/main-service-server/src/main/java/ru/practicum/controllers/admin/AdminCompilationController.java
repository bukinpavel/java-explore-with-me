package ru.practicum.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.CompilationNewDto;
import ru.practicum.model.dto.CompilationUpdateDto;
import ru.practicum.services.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody CompilationNewDto compilationDto) {
        log.info("Создание подборки событий - {}", compilationDto);
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationById(@PathVariable Long compId) {
        log.info("Удаление подборки событий - {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilationById(@PathVariable Long compId,
                                                @Valid @RequestBody CompilationUpdateDto updateDto) {
        log.info("Обновление подборки событий - {} c id - {}", compId, compId);
        return compilationService.updateCompilation(compId, updateDto);
    }

}



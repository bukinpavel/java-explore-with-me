package ru.main_service.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.main_service.exceptions.NotFoundException;
import ru.main_service.mappers.CompilationMapper;
import ru.main_service.mappers.EventMapper;
import ru.main_service.model.Compilation;
import ru.main_service.model.Event;
import ru.main_service.model.dto.CompilationDto;
import ru.main_service.model.dto.CompilationNewDto;
import ru.main_service.model.dto.CompilationUpdateDto;
import ru.main_service.model.dto.EventShortDto;
import ru.main_service.repositories.CompilationRepository;
import ru.main_service.repositories.EventRepository;
import ru.main_service.repositories.ParticipationRepository;
import ru.main_service.services.CompilationService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    @Transactional
    @Override
    public CompilationDto createCompilation(CompilationNewDto compilationNewDto) {
        Compilation compilationNew = CompilationMapper.mapToModel(compilationNewDto);
        List<Event> events = new ArrayList<>();
        if (compilationNewDto.getEvents() != null) {
            for (Long eventId : compilationNewDto.getEvents()) {
                if (eventRepository.findById(eventId).isPresent()) {
                    events.add(eventRepository.getReferenceById(eventId));
                }
            }
        }
        compilationNew.setEvents(events);
        if (compilationNewDto.getPinned() == null) compilationNew.setPinned(false);
        Compilation compilation = compilationRepository.save(compilationNew);
        List<EventShortDto> shortEvents = compilation.getEvents()
                .stream().map(event -> EventMapper
                        .mapToShortDto(event, participationRepository.getConfirmedRequests(event.getId())))
                .collect(Collectors.toList());
        return CompilationMapper.mapToDto(compilation, shortEvents);
    }

    @Override
    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            throw new NotFoundException("Подборка с id-" + id + " не найдена");
        }
        compilationRepository.deleteById(id);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Compilation> compilations = new ArrayList<>();
        List<CompilationDto> compilationsDto = new ArrayList<>();
        if (pinned == null) {
            compilations.addAll(compilationRepository.findAll(pageable).getContent());
            for (Compilation comp : compilations) {
                List<Event> events = new ArrayList<>(comp.getEvents());
                List<EventShortDto> shortEvents = events
                        .stream().map(event -> EventMapper
                                .mapToShortDto(event, participationRepository.getConfirmedRequests(event.getId())))
                        .collect(Collectors.toList());
                compilationsDto.add(CompilationMapper.mapToDto(comp, shortEvents));
            }
            return compilationsDto;
        }
        compilations.addAll(compilationRepository.findAllByPinned(pinned, pageable));
        for (Compilation comp : compilations) {
            List<Event> events = new ArrayList<>(comp.getEvents());
            List<EventShortDto> shortEvents = events
                    .stream().map(event -> EventMapper
                            .mapToShortDto(event, participationRepository.getConfirmedRequests(event.getId())))
                    .collect(Collectors.toList());
            compilationsDto.add(CompilationMapper.mapToDto(comp, shortEvents));
        }
        return compilationsDto;
    }

    @Override
    public CompilationDto getCompilation(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка с id-" + id + " не найдена"));
        List<Event> events = compilation.getEvents();
        List<EventShortDto> shortEvents = events
                .stream().map(event -> EventMapper
                        .mapToShortDto(event, participationRepository.getConfirmedRequests(event.getId())))
                .collect(Collectors.toList());
        return CompilationMapper.mapToDto(compilation, shortEvents);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long id, CompilationUpdateDto updateDto) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка событие с id-" + id + " не найдена"));
        List<Event> eventsOld = compilation.getEvents();
        List<Event> eventsNew = new ArrayList<>();
        if (updateDto.getEvents() != null) {
            for (Long eventId : updateDto.getEvents()) {
                if (eventRepository.findById(eventId).isPresent()) {
                    eventsNew.add(eventRepository.getReferenceById(eventId));
                }
            }
        }
        eventsOld.addAll(eventsNew);
        List<Event> eventsActual = eventsOld
                .stream()
                .distinct()
                .collect(Collectors.toList());
        compilation.setEvents(eventsActual);
        if (updateDto.getTitle() != null) compilation.setTitle(updateDto.getTitle());
        if (updateDto.getPinned() != null) compilation.setPinned(updateDto.getPinned());
        Compilation compilationUpdate = compilationRepository.save(compilation);
        List<EventShortDto> shortEvents = compilationUpdate.getEvents()
                .stream().map(event -> EventMapper
                        .mapToShortDto(event, participationRepository.getConfirmedRequests(event.getId())))
                .collect(Collectors.toList());
        return CompilationMapper.mapToDto(compilationUpdate, shortEvents);
    }

}


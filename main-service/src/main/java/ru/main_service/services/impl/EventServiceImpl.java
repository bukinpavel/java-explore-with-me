package ru.main_service.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.main_service.ConfigClient;
import ru.main_service.exceptions.EventException;
import ru.main_service.exceptions.NotFoundException;
import ru.main_service.exceptions.RequestException;
import ru.main_service.mappers.EventMapper;
import ru.main_service.mappers.ParticipationMapper;
import ru.main_service.model.Category;
import ru.main_service.model.Event;
import ru.main_service.model.Participation;
import ru.main_service.model.dto.*;
import ru.main_service.repositories.CategoryRepository;
import ru.main_service.repositories.EventRepository;
import ru.main_service.repositories.ParticipationRepository;
import ru.main_service.repositories.UserRepository;
import ru.main_service.services.EventService;
import ru.stat_dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ConfigClient client;

    @Override
    public EventFullDto createEvent(EventNewDto eventDto, Long userId) {
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                new RequestException("Категория с id-" + eventDto.getCategory() + " не найдена"));
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new RequestException("Время начала события не может быть раньше, чем через 2 часа");
        }
        if (eventDto.getPaid() == null) eventDto.setPaid(false);
        if (eventDto.getParticipantLimit() == null) eventDto.setParticipantLimit(0);
        if (eventDto.getRequestModeration() == null) eventDto.setRequestModeration(true);
        Event event = EventMapper.mapToModel(eventDto, category, userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id-" + userId + " не найден")));
        event.setCreationOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
        return EventMapper.mapToFullDto(eventRepository.save(event), 0);
    }

    @Transactional
    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        }
        Pageable page = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findAllByInitiator_Id(userId, page);

        return events.stream().map(event -> EventMapper.mapToShortDto(event, participationRepository
                        .getConfirmedRequests(event.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с id-" + eventId + " не найдено");
        }
        return EventMapper.mapToFullDto(eventRepository.findByIdAndInitiator_Id(eventId, userId),
                participationRepository.getConfirmedRequests(eventId));
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminRequestDto eventDto) {
        Event adminUpdateEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id-" + eventId + " не найдено"));
        if (adminUpdateEvent.getState() == (EventState.PUBLISHED) ||
                adminUpdateEvent.getState() == (EventState.CANCELED)) {
            throw new DataIntegrityViolationException("Событие нельзя изменить, т.к. оно уже опубликовано/отменено");
        }
        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction() == StateAction.PUBLISH_EVENT) adminUpdateEvent.setState(EventState.PUBLISHED);
            if (eventDto.getStateAction() == StateAction.REJECT_EVENT) adminUpdateEvent.setState(EventState.CANCELED);
        }
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().equals(adminUpdateEvent.getAnnotation())) {
            adminUpdateEvent.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null && !eventDto.getCategory().equals(adminUpdateEvent.getCategory().getId())) {
            adminUpdateEvent.setCategory(categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория с id-" + eventDto.getCategory() + " не найдена")));
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().equals(adminUpdateEvent.getDescription())) {
            adminUpdateEvent.setDescription(eventDto.getDescription());
        }

        if (eventDto.getEventDate() != null && !eventDto.getEventDate().equals(adminUpdateEvent.getEventDate())) {
            adminUpdateEvent.setEventDate(eventDto.getEventDate());
        }
        if (eventDto.getPaid() != null && eventDto.getPaid() != (adminUpdateEvent.getPaid())) {
            adminUpdateEvent.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null && !eventDto.getParticipantLimit().equals(adminUpdateEvent
                .getParticipantLimit())) {
            adminUpdateEvent.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().equals(adminUpdateEvent.getTitle())) {
            adminUpdateEvent.setTitle(eventDto.getTitle());
        }
        if (eventDto.getLocation() != null && eventDto.getLocation().getLat() != null &&
                !eventDto.getLocation().getLat().equals(adminUpdateEvent.getLatitude())) {
            adminUpdateEvent.setLatitude(eventDto.getLocation().getLat());
        }
        if (eventDto.getLocation() != null && eventDto.getLocation().getLon() != null &&
                !eventDto.getLocation().getLon().equals(adminUpdateEvent.getLongitude())) {
            adminUpdateEvent.setLongitude(eventDto.getLocation().getLon());
        }
        Event event = eventRepository.save(adminUpdateEvent);
        return EventMapper.mapToFullDto(event,
                participationRepository.getConfirmedRequests(event.getId()));
    }

    @Override
    public List<EventFullDto> getAllEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeStart = LocalDateTime.now().plusYears(100);
        return eventRepository.getAllUsersEvents(users, categories, states, rangeStart, rangeEnd, pageable)
                .stream()
                .map(event -> EventMapper.mapToFullDto(event, participationRepository
                        .getConfirmedRequests(event.getId()))).collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getAllEventsPublic(String text, List<Long> catIds, Boolean paid, LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                                  Integer from, Integer size, HttpServletRequest request) {

        if (Objects.isNull(rangeStart)) rangeStart = LocalDateTime.now();
        if (Objects.isNull(rangeEnd)) rangeEnd = LocalDateTime.now().plusYears(100);
        if (rangeEnd.isBefore(rangeStart)) throw new RequestException("Неверный временной диапазон");
        Pageable pageable = PageRequest.of(from, size);
        List<Event> sortedEvents = new ArrayList<>();
        if (sort.equals("EVENT_DATE"))
            sortedEvents = eventRepository.getFilterEventsByDate(text, catIds, paid, rangeStart, rangeEnd, pageable);
        if (sort.equals("VIEWS"))
            sortedEvents = eventRepository.getFilterEventsByViews(text, catIds, paid, rangeStart, rangeEnd, pageable);
        if (onlyAvailable) {
            sortedEvents.removeIf(event ->
                    participationRepository.getConfirmedRequests(event.getId()) >= event.getParticipantLimit());
        }
        client.statClient().hit(request);
        Map<Long, Long> stats = getViewStats(sortedEvents);
        return sortedEvents.stream()
                .peek(event -> event.setViews(stats.get(event.getId())))
                .map(event -> EventMapper
                        .mapToShortDto(event, participationRepository.getConfirmedRequests(event.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request) {
        if (!eventRepository.existsById(eventId))
            throw new NotFoundException("Событие с id-" + eventId + " не найдено");
        if (!eventRepository.getReferenceById(eventId).getState().equals(EventState.PUBLISHED))
            throw new NotFoundException("Событие еще не опубликовано");
        Event event = eventRepository.getReferenceById(eventId);
        client.statClient().hit(request);
        Map<Long, Long> stats = getViewStats(List.of(event));
        if (!stats.isEmpty()) event.setViews(stats.get(event.getId()));
        eventRepository.save(event);
        return EventMapper.mapToFullDto(event,
                participationRepository.getConfirmedRequests(event.getId()));
    }


    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, EventUpdateUserRequestDto updateDto) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        Event updateEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id-" + eventId + " не найдено"));
        if (updateEvent.getState() == (EventState.PUBLISHED))
            throw new DataIntegrityViolationException("Событие нельзя изменить, т.к. оно уже опубликованно");
        if (updateDto.getStateAction() != null) {
            if (updateDto.getStateAction() == StateAction.SEND_TO_REVIEW) updateEvent.setState(EventState.PENDING);
            if (updateDto.getStateAction() == StateAction.CANCEL_REVIEW) updateEvent.setState(EventState.CANCELED);
        }
        if (updateDto.getEventDate() != null) {
            if (updateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new EventException("Время начала события не может быть раньше, чем через 2 часа");
            }
        }
        if (updateDto.getAnnotation() != null) updateEvent.setAnnotation(updateDto.getAnnotation());
        if (updateDto.getCategory() != null)
            updateEvent.setCategory(categoryRepository.findById(updateDto.getCategory()).orElseThrow(() ->
                    new NotFoundException("Категория не найдена")));
        if (updateDto.getDescription() != null) updateEvent.setDescription(updateDto.getDescription());
        if (updateDto.getEventDate() != null && !updateDto.getEventDate().equals(updateEvent
                .getEventDate())) {
            updateEvent.setEventDate(updateDto.getEventDate());
        }
        if (updateDto.getPaid() != null && updateDto.getPaid() != (updateEvent.getPaid())) {
            updateEvent.setPaid(updateDto.getPaid());
        }
        if (updateDto.getParticipantLimit() != null && !updateDto.getParticipantLimit()
                .equals(updateEvent.getParticipantLimit())) {
            updateEvent.setParticipantLimit(updateDto.getParticipantLimit());
        }
        if (updateDto.getTitle() != null && !updateDto.getTitle().equals(updateEvent.getTitle())) {
            updateEvent.setTitle(updateDto.getTitle());
        }
        Event event = eventRepository.save(updateEvent);
        return EventMapper.mapToFullDto(event,
                participationRepository.getConfirmedRequests(event.getId()));
    }

    @Transactional
    public List<ParticipationRequestDto> getParticipantsInEventByUser(Long userId, Long eventId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        if (!eventRepository.existsById(eventId))
            throw new NotFoundException("Событие с id-" + eventId + " не найдено");
        List<Participation> participation = participationRepository
                .findAllByEvent_IdAndEvent_Initiator_Id(eventId, userId);
        return participation.stream().map(ParticipationMapper::mapToDto)
                .collect(Collectors.toList());

    }

    @Transactional
    public ChangeRequestDto updateParticipantsByUser(Long userId,
                                                     Long eventId,
                                                     ChangeRequestDto requestDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id-" + eventId + " не найдено"));
        if (!Objects.equals(userId, event.getInitiator().getId()))
            throw new EventException("Пользователь не является инициатором текущего события");
        if (event.getParticipantLimit() == 0 || (!event.getRequestModeration()))
            throw new EventException("Одобрение заявок данного события не требуется");
        if (participationRepository.getConfirmedRequests(eventId) >= event.getParticipantLimit())
            throw new DataIntegrityViolationException("Лимит по заявкам исчерпан");
        List<Participation> participation = participationRepository
                .findAllParticipation(requestDto.getRequestIds());
        if (participation != null) {
            for (Participation request : participation) {
                if (request.getStatus() == RequestStatus.PENDING) {
                    if (participationRepository.getConfirmedRequests(eventId) <= event.getParticipantLimit()) {
                        request.setStatus(requestDto.getStatus());
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                    }
                } else {
                    throw new EventException("Нельзя изменить статус заявки пользователя" +
                            request.getRequester().getId());
                }
            }
        } else {
            throw new EventException("Заявки не найдены");
        }

        List<ParticipationRequestDto> participationNew = participationRepository.saveAll(participation)
                .stream()
                .map(ParticipationMapper::mapToDto)
                .collect(Collectors.toList());
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequestDto request : participationNew) {
            if (request.getStatus().equals(String.valueOf(RequestStatus.CONFIRMED))) {
                confirmedRequests.add(request);
            } else if (request.getStatus().equals(String.valueOf(RequestStatus.REJECTED))) {
                rejectedRequests.add(request);
            }
        }
        requestDto.setConfirmedRequests(confirmedRequests);
        requestDto.setRejectedRequests(rejectedRequests);
        return requestDto;
    }

    private Map<Long, Long> getViewStats(Collection<Event> eventList) {
        List<String> uris = eventList.stream()
                .map(Event::getId)
                .map(id -> "/events/" + id.toString())
                .collect(Collectors.toList());
        List<ViewStats> viewStats = client.statClient().getListStats(LocalDateTime.now().minusYears(1L),
                LocalDateTime.now().plusYears(1L), uris, true);
        return viewStats.stream()
                .filter(statRecord -> statRecord.getApp().equals("ewm-service"))
                .collect(Collectors.toMap(
                                statRecord -> parseId(statRecord.getUri()),
                                ViewStats::getHits
                        )
                );
    }

    private Long parseId(String str) {
        int index = str.lastIndexOf('/');
        String strId = str.substring(index + 1);
        log.info("String STR ID:" + strId);
        return Long.parseLong(strId);
    }
}

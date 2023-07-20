package ru.main_service.services;

import ru.main_service.model.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getAllEventsPublic(String text, List<Long> catIds, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                           String sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getPublishedEventById(Long eventId, HttpServletRequest request);

    EventFullDto createEvent(EventNewDto eventDto, Long userId);

    List<EventShortDto> getEventsByUserId(Long userId, int from, int size);

    EventFullDto updateEventByUser(Long userId, Long eventId, EventUpdateUserRequestDto updateEvent);

    EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequestDto> getParticipantsInEventByUser(Long userId, Long eventId);

    ChangeRequestDto updateParticipantsByUser(Long userId,
                                              Long eventId,
                                              ChangeRequestDto requestDto);

    List<EventFullDto> getAllEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, EventUpdateAdminRequestDto eventDto);

}

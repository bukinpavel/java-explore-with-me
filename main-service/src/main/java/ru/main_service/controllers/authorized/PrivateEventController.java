package ru.main_service.controllers.authorized;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import ru.main_service.model.dto.*;
import ru.main_service.services.EventService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping()
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        log.info("Получение события с Id {}", userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId, @Valid @RequestBody EventNewDto eventDto) {
        log.info("Создание события {} пользователем {}", eventDto, userId);
        return eventService.createEvent(eventDto, userId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getInfoAboutEventByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение полной информации о событии пользователем {}", userId);
        return eventService.getEventByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                          @Valid @RequestBody EventUpdateUserRequestDto requestDto) {
        log.info("Обновление события с Id {} данными {}", eventId, requestDto);
        return eventService.updateEventByUser(userId, eventId, requestDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getInfoAboutRequestByEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение информации об участии пользователя с Id {} в событии Id {}", userId, eventId);
        return eventService.getParticipantsInEventByUser(userId, eventId);
    }


    @PatchMapping("/{eventId}/requests")
    public ChangeRequestDto changeStatusByUser(@PathVariable Long userId, @PathVariable Long eventId,
                                               @RequestBody ChangeRequestDto requestDto) {
        log.info("Обновление события пользователем Id {} данными {}", userId, requestDto);
        return eventService.updateParticipantsByUser(userId, eventId,
                requestDto);
    }
}

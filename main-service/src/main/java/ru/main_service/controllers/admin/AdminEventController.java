package ru.main_service.controllers.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.main_service.model.dto.EventFullDto;
import ru.main_service.model.dto.EventState;
import ru.main_service.model.dto.EventUpdateAdminRequestDto;
import ru.main_service.services.EventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEventsAdmin(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false) @DateTimeFormat(pattern =
                                                        "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false) @DateTimeFormat(pattern =
                                                        "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        log.info("Получение полной информации о событиях администратором");
        List<EventState> statesReform;
        if (states != null) {
            statesReform = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        } else {
            statesReform = null;
        }
        return eventService.getAllEventsAdmin(users, statesReform, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId, @Valid @RequestBody EventUpdateAdminRequestDto eventDto) {
        log.info("Обновление событие администратором : {}", eventDto);
        return eventService.updateEventByAdmin(eventId, eventDto);
    }


}

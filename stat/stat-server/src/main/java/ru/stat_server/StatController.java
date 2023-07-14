package ru.stat_server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.stat_dto.EndpointHitDto;
import ru.stat_dto.ViewStats;
import ru.stat_server.exception.RequestException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatController {

    private final StatService statService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/hit")
    public void addEndpointHit(@RequestBody EndpointHitDto hit) {
        log.info("Сохранение запроса пользователя {}", hit);
        statService.addEndpointHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getViews(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        if (start.isAfter(LocalDateTime.now()))
            throw new RequestException("Не валидные данные");
        log.info("Показать статистику");
        return statService.getHits(start, end, uris, unique);
    }


}


package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.service.StatsService;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
}

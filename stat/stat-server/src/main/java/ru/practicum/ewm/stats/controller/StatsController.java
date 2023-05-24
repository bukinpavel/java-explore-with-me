package ru.practicum.ewm.stats.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;
import ru.practicum.ewm.stats.service.StatsService;

import java.util.List;

@RestController
public class StatsController {
    private final StatsService statsService;

    @Autowired
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping(value = "/hit")
    public void addHit(@RequestBody ViewsStatsRequest request) {
        statsService.postHit(request);
    }

    @GetMapping(value = "/stats")
    public List<ViewStats> getStats(@RequestParam("start") String start, @RequestParam("end") String end,
                                    @RequestParam(value = "uris", required = false) String[] uris,
                                    @RequestParam(value = " unique", required = false) String unique) {
        return statsService.getHit(start, end, uris, unique);
    }
}

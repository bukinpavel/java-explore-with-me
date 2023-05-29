package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;
import java.util.List;

public interface StatsService {
    void postHit(ViewsStatsRequest request);

    List<ViewStats> getHit(String start, String end, String[] uris, String unique);
}

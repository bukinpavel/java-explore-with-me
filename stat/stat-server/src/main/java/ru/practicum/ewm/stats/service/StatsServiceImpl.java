package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewsStatsRequest;
import ru.practicum.ewm.stats.repository.StatsRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void postHit(ViewsStatsRequest request) {
        statsRepository.save(request.mapToEndpointHit(request));
    }

    @Override
    public List<ViewStats> getHit(String start, String end, String[] uris, String unique) {
        if (unique == null && uris == null) {
            return statsRepository.returnDates(parseDateTime(start), parseDateTime(end));
        } else if (unique != null && uris == null) {
            if (unique.equals("true")) {
                return statsRepository.returnDatesAndUnique(parseDateTime(start), parseDateTime(end));
            } else {
                return statsRepository.returnDates(parseDateTime(start), parseDateTime(end));
            }
        } else if (unique == null) {
            return statsRepository.returnDatesAndUris(parseDateTime(start), parseDateTime(end), uris);
        } else {
            if (unique.equals("true")) {
                return statsRepository.returnDatesAndUrisAndUnique(parseDateTime(start), parseDateTime(end), uris);
            } else {
                return statsRepository.returnDatesAndUris(parseDateTime(start), parseDateTime(end), uris);
            }
        }
    }

    private LocalDateTime parseDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

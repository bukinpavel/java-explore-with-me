package ru.stat_server;

import org.springframework.stereotype.Service;
import ru.stat_dto.EndpointHitDto;
import ru.stat_dto.ViewStats;


import java.time.LocalDateTime;
import java.util.List;

@Service
public interface StatService {

    void addEndpointHit(EndpointHitDto hit);

    List<ViewStats> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

}

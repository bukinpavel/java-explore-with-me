package ru.stat_server;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.stat_dto.EndpointHitDto;
import ru.stat_dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public void addEndpointHit(EndpointHitDto hit) {
        statRepository.save(StatMapper.endpointHitDtoToModel(hit));
    }

    @Override
    public List<ViewStats> getHits(LocalDateTime start,
                                   LocalDateTime end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStats;
        if (unique) {
            viewStats = statRepository.getByUriAndUniqueIp(start, end, uris);
        } else {
            viewStats = statRepository.getByUri(start, end, uris);
        }
        return viewStats;
    }
}

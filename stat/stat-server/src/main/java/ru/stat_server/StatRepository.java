package ru.stat_server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stat_dto.ViewStats;
import ru.stat_server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select new ru.stat_dto.ViewStats(i.app, i.uri, count(distinct i.ip)) " +
            "from EndpointHit i where i.timestamp between ?1 and ?2 " +
            "and i.uri in ?3 or (?3) is null " +
            "group by i.app, i.uri " +
            "order by count(distinct i.ip) desc")
    List<ViewStats> getByUriAndUniqueIp(LocalDateTime startDate, LocalDateTime endDate, List<String> uris);

    @Query(value = "select new ru.stat_dto.ViewStats(i.app, i.uri, count(i.ip)) " +
            "from EndpointHit i where i.timestamp between ?1 and ?2 " +
            "and i.uri in ?3 or (?3) is null " +
            "group by i.app, i.uri " +
            "order by count(i.ip) desc")
    List<ViewStats> getByUri(LocalDateTime startDate, LocalDateTime endDate, List<String> uris);

}


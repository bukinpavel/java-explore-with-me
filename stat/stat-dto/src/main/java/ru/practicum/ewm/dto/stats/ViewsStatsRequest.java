package ru.practicum.ewm.dto.stats;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewsStatsRequest {
    String app;
    String uri;
    String ip;
    String timestamp;

    public EndpointHit mapToEndpointHit(ViewsStatsRequest viewsStatsRequest) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setApp(viewsStatsRequest.getApp());
        endpointHit.setUri(viewsStatsRequest.getUri());
        endpointHit.setIp(viewsStatsRequest.getIp());
        endpointHit.setTimestamp(LocalDateTime.parse(viewsStatsRequest.getTimestamp(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return endpointHit;
    }
}

package ru.stat_server;

import ru.stat_dto.EndpointHitDto;
import ru.stat_server.model.EndpointHit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatMapper {

    public static EndpointHit endpointHitDtoToModel(EndpointHitDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EndpointHit hit = new EndpointHit();
        hit.setApp(dto.getApp());
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimestamp(LocalDateTime.parse(dto.getTimestamp(), formatter));
        return hit;
    }
}

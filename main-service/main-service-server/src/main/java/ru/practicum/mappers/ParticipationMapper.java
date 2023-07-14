package ru.practicum.mappers;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.model.Participation;
import ru.practicum.model.User;
import ru.practicum.model.Event;
import ru.practicum.model.dto.ParticipationRequestDto;
import ru.practicum.model.dto.RequestStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ParticipationMapper {

    public static ParticipationRequestDto mapToDto(Participation participation) {
        ParticipationRequestDto participationDto = new ParticipationRequestDto();
        participationDto.setId(participation.getId());
        participationDto.setCreated(participation.getCreated());
        participationDto.setEvent(participation.getEvent().getId());
        participationDto.setRequester(participation.getRequester().getId());
        participationDto.setStatus(String.valueOf(participation.getStatus()));
        return participationDto;
    }

    public static Participation mapToModel(ParticipationRequestDto participationDto, User requester, Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = participationDto.getCreated().format(formatter);
        Participation participation = new Participation();
        participation.setId(participationDto.getId());
        participation.setCreated(LocalDateTime.parse(time, formatter));
        participation.setRequester(requester);
        participation.setStatus(RequestStatus.valueOf(participationDto.getStatus()));
        participation.setEvent(event);
        return participation;
    }

}


package ru.practicum.services;

import ru.practicum.model.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {

    List<ParticipationRequestDto> getAllRequestsByUser(Long userId);

    ParticipationRequestDto addRequestByUserForEvent(Long userId, Long eventId);

    ParticipationRequestDto canselRequestByUserForEvent(Long userId, Long eventId);

}


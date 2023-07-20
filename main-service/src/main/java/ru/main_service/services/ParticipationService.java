package ru.main_service.services;

import ru.main_service.model.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {

    List<ParticipationRequestDto> getAllRequestsByUser(Long userId);

    ParticipationRequestDto addRequestByUserForEvent(Long userId, Long eventId);

    ParticipationRequestDto canselRequestByUserForEvent(Long userId, Long eventId);

}

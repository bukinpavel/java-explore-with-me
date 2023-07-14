package ru.main_service.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.main_service.exceptions.NotFoundException;
import ru.main_service.exceptions.RequestException;
import ru.main_service.mappers.ParticipationMapper;
import ru.main_service.model.Event;
import ru.main_service.model.Participation;
import ru.main_service.model.User;
import ru.main_service.model.dto.EventState;
import ru.main_service.model.dto.ParticipationRequestDto;
import ru.main_service.model.dto.RequestStatus;
import ru.main_service.repositories.EventRepository;
import ru.main_service.repositories.ParticipationRepository;
import ru.main_service.repositories.UserRepository;
import ru.main_service.services.ParticipationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public ParticipationRequestDto addRequestByUserForEvent(Long userId, Long eventId) {
        validRequest(userId, eventId);
        User user = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        if (participationRepository.findByRequester_IdAndEvent_Id(userId, eventId) != null)
            throw new DataIntegrityViolationException("Запрос на участие в событии: " + event.getTitle() + " от пользователя с id-"
                    + userId + " уже существует");
        if (event.getInitiator().getId().equals(userId))
            throw new DataIntegrityViolationException("Инициатор не может подать заявку на своё событие");
        if (!event.getState().equals(EventState.PUBLISHED))
            throw new DataIntegrityViolationException("Нельзя подать заявку на неопубликованное событие");
        if (event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit().equals(participationRepository
                    .getConfirmedRequests(eventId))) {
                throw new DataIntegrityViolationException("Лимит заявок на событие исчерпан");
            }
        }
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setRequester(userId);
        participationRequestDto.setEvent(eventId);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participationRequestDto.setStatus(String.valueOf(RequestStatus.CONFIRMED));
        } else {
            participationRequestDto.setStatus(String.valueOf(RequestStatus.PENDING));
        }
        participationRequestDto.setCreated(LocalDateTime.now());
        Participation participation = participationRepository
                .save(ParticipationMapper.mapToModel(participationRequestDto, user, event));
        return ParticipationMapper.mapToDto(participation);
    }


    @Override
    public List<ParticipationRequestDto> getAllRequestsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        }
        List<Participation> requests = participationRepository.findAllByRequester_Id(userId);
        return requests.stream()
                .map(ParticipationMapper::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    public ParticipationRequestDto canselRequestByUserForEvent(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        }
        Participation participation = participationRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id-" + requestId + " не найден"));
        if (participation.getStatus() == RequestStatus.PENDING) {
            participation.setStatus(RequestStatus.CANCELED);
            return ParticipationMapper.mapToDto(participationRepository.save(participation));
        } else {
            throw new RequestException("Статус заявки находится на в ожидании. Статус - " + participation.getStatus());
        }
    }

    private void validRequest(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id-" + userId + "не найден"));
        eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id-" + eventId + " не найдено"));
    }
}


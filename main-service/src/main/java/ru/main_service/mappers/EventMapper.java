package ru.main_service.mappers;

import lombok.extern.slf4j.Slf4j;
import ru.main_service.model.Category;
import ru.main_service.model.Event;
import ru.main_service.model.User;
import ru.main_service.model.dto.EventFullDto;
import ru.main_service.model.dto.EventNewDto;
import ru.main_service.model.dto.EventShortDto;

import java.time.format.DateTimeFormatter;

@Slf4j
public class EventMapper {

    public static Event mapToModel(EventNewDto eventDto, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDto.getEventDate());
        event.setLatitude(eventDto.getLocation().getLat());
        event.setLongitude(eventDto.getLocation().getLon());
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setTitle(eventDto.getTitle());
        event.setInitiator(initiator);
        return event;
    }

    public static EventFullDto mapToFullDto(Event event, Integer confirmedRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        EventFullDto eventFullDto = new EventFullDto();

        EventFullDto.Location location = new EventFullDto.Location();

        location.setLat(event.getLatitude());
        location.setLon(event.getLongitude());
        eventFullDto.setLocation(location);

        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setConfirmedRequests(confirmedRequest);
        eventFullDto.setCategory(CategoryMapper.mapToDto(event.getCategory()));
        eventFullDto.setCreatedOn(event.getCreationOn());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setInitiator(UserMapper.mapToDto(event.getInitiator()));
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState().name());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(event.getViews());

        return eventFullDto;
    }

    public static EventShortDto mapToShortDto(Event event, Integer confirmedRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        EventShortDto eventDto = new EventShortDto();
        eventDto.setId(event.getId());
        eventDto.setEventDate(event.getEventDate().format(formatter));
        eventDto.setAnnotation(event.getAnnotation());
        eventDto.setCategory(CategoryMapper.mapToDto(event.getCategory()));
        eventDto.setConfirmedRequests(confirmedRequest);
        eventDto.setInitiator(UserMapper.mapToDto(event.getInitiator()));
        eventDto.setPaid(event.getPaid());
        eventDto.setViews(event.getViews());
        eventDto.setTitle(event.getTitle());
        return eventDto;
    }

}

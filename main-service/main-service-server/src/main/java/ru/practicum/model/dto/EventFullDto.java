package ru.practicum.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {

    Long id;

    String annotation;

    CategoryDto category;

    Integer confirmedRequests;

    LocalDateTime createdOn;

    String description;

    String eventDate;

    UserDto initiator;

    Location location;

    Boolean paid;

    Integer participantLimit;

    LocalDateTime publishedOn;

    Boolean requestModeration;

    String state;

    String title;

    Long views;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Location {

        Double lat;

        Double lon;
    }
}


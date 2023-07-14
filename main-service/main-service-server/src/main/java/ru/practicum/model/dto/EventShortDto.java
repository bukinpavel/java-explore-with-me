package ru.practicum.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {

    Long id;

    String annotation;

    Integer confirmedRequests;

    CategoryDto category;

    String eventDate;

    UserDto initiator;

    Boolean paid;

    String title;

    Long views;
}


package ru.practicum.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    String annotation;
    Integer category;
    @NotBlank
    @Length(min = 20, max = 7000)
    String description;
    @NotBlank
    @Length(min = 3, max = 120)
    String title;
    String eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
}

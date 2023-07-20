package ru.main_service.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {

    Long id;

    List<EventShortDto> events;

    Boolean pinned;

    String title;

}

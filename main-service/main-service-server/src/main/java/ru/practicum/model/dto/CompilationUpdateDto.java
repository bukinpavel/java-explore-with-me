package ru.practicum.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationUpdateDto {
    List<Long> events;

    Boolean pinned;

    @Size(min = 1, max = 50)
    String title;
}


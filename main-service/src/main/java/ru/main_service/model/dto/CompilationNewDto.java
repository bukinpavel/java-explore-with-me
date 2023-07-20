package ru.main_service.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationNewDto {

    List<Long> events;

    Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    String title;

}

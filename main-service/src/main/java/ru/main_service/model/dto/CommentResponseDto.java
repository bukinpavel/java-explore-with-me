package ru.main_service.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponseDto {

    Long id;

    String titleEvent;

    String userName;

    String comment;

    LocalDateTime commentDate;
}

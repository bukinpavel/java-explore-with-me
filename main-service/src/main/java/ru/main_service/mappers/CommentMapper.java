package ru.main_service.mappers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.main_service.model.Comment;
import ru.main_service.model.dto.CommentRequestDto;
import ru.main_service.model.dto.CommentResponseDto;

import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public final class CommentMapper {

    public static CommentResponseDto mapToDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .titleEvent(comment.getEvent().getTitle())
                .userName(comment.getUser().getName())
                .comment(comment.getComment())
                .commentDate(comment.getCommentDate())
                .build();

    }

    public static Comment mapToModel(CommentRequestDto commentDto) {
        return Comment.builder()
                .comment(commentDto.getComment())
                .commentDate(LocalDateTime.now())
                .build();
    }
}

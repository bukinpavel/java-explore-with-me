package ru.practicum.mappers;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.model.Comment;
import ru.practicum.model.dto.CommentRequestDto;
import ru.practicum.model.dto.CommentResponseDto;

import java.time.LocalDateTime;

@Slf4j
public class CommentMapper {

    public static CommentResponseDto mapToDto(Comment comment) {
        CommentResponseDto commentDto = new CommentResponseDto();
        commentDto.setId(comment.getId());
        commentDto.setTitleEvent(comment.getEvent().getTitle());
        commentDto.setUserName(comment.getUser().getName());
        commentDto.setComment(comment.getComment());
        commentDto.setCommentDate(comment.getCommentDate());
        return commentDto;
    }

    public static Comment mapToModel(CommentRequestDto commentDto) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        comment.setCommentDate(LocalDateTime.now());
        return comment;
    }
}


package ru.main_service.services;

import ru.main_service.model.dto.CommentRequestDto;
import ru.main_service.model.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto createComment(CommentRequestDto commentDto, Long userId, Long eventId);

    CommentResponseDto updateComment(CommentRequestDto commentDto, Long userId, Long eventId);

    void deleteComment(Long userId, Long eventId);

    List<CommentResponseDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size);

    List<CommentResponseDto> getAllCommentsByUser(Long userId, Integer from, Integer size);

}


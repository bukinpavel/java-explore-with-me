package ru.main_service.controllers.authorized;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.main_service.model.dto.CommentRequestDto;
import ru.main_service.model.dto.CommentResponseDto;
import ru.main_service.services.CommentService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users/comments/{userId}/{eventId}")
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @Valid @RequestBody CommentRequestDto commentDto) {
        log.info("Создание комментария пользователем с id - {} на событие c id - {}", userId, eventId);
        return commentService.createComment(commentDto, userId, eventId);
    }

    @PatchMapping()
    public CommentResponseDto updateComment(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @Valid @RequestBody CommentRequestDto commentDto) {
        log.info("Редактирование комментария пользователем с id - {} события c id - {}", userId, eventId);
        return commentService.updateComment(commentDto, userId, eventId);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId) {
        log.info("Удаление комментария с id - {} пользователем - {}", eventId, userId);
        commentService.deleteComment(userId, eventId);
    }
}


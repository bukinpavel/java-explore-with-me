package ru.main_service.controllers.nonauthorized;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.main_service.model.dto.CommentResponseDto;
import ru.main_service.services.CommentService;

import java.util.List;

@RestController
@RequestMapping(path = "/comments")
@RequiredArgsConstructor
@Slf4j
public class PublicCommentController {

    private final CommentService commentService;

    @GetMapping("/events/{eventId}")
    public List<CommentResponseDto> getAllCommentsByEvent(
            @PathVariable Long eventId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение комментариев события с id - {}", eventId);
        return commentService.getAllCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/users/{userId}")
    public List<CommentResponseDto> getAllCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение комментариев по пользователя с id - {}", userId);
        return commentService.getAllCommentsByUser(userId, from, size);
    }
}

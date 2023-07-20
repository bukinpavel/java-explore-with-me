package ru.main_service.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.main_service.exceptions.NotFoundException;
import ru.main_service.mappers.CommentMapper;
import ru.main_service.model.Comment;
import ru.main_service.model.Event;
import ru.main_service.model.User;
import ru.main_service.model.dto.CommentRequestDto;
import ru.main_service.model.dto.CommentResponseDto;
import ru.main_service.repositories.CommentRepository;
import ru.main_service.repositories.EventRepository;
import ru.main_service.repositories.UserRepository;
import ru.main_service.services.CommentService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto createComment(CommentRequestDto commentDto, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие с id-" + eventId + " не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id-" + userId + " не найден"));
        Comment comment = CommentMapper.mapToModel(commentDto);
        comment.setEvent(event);
        comment.setUser(user);
        return CommentMapper.mapToDto(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto updateComment(CommentRequestDto commentDto, Long userId, Long eventId) {
        Comment comment = commentRepository.findByUser_IdAndEvent_Id(userId, eventId);
        if (comment == null)
            throw new NotFoundException("Такого комментария не существует");
        comment.setComment(commentDto.getComment());
        return CommentMapper.mapToDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void deleteComment(Long userId, Long eventId) {
        Comment comment = commentRepository.findByUser_IdAndEvent_Id(userId, eventId);
        if (comment == null)
            throw new NotFoundException("Такого комментария не существует");
        commentRepository.deleteByUser_IdAndEvent_Id(userId, eventId);
    }

    @Override
    public List<CommentResponseDto> getAllCommentsByEvent(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByEvent_IdOrderByCommentDateDesc(eventId, pageable);
        return comments.stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getAllCommentsByUser(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        List<Comment> comments = commentRepository.findAllByUser_IdOrderByCommentDateDesc(userId, pageable);
        return comments.stream()
                .map(CommentMapper::mapToDto)
                .collect(Collectors.toList());
    }
}

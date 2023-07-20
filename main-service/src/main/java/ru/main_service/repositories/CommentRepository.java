package ru.main_service.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.main_service.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByUser_IdAndEvent_Id(Long userId, Long eventId);

    void deleteByUser_IdAndEvent_Id(Long userId, Long eventId);

    List<Comment> findAllByEvent_IdOrderByCommentDateDesc(Long eventId, Pageable page);

    List<Comment> findAllByUser_IdOrderByCommentDateDesc(Long userId, Pageable page);
}

package ru.practicum.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Event;
import ru.practicum.model.dto.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiator_Id(Long id, Pageable page);

    Event findByIdAndInitiator_Id(Long id, Long initiator);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (e.initiator) IS NOT NULL OR e.initiator.id IN :users " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:catIds) IS NULL OR e.category.id IN :catIds) " +
            "AND e.eventDate >= :rangeStart " +
            "AND e.eventDate <= :rangeEnd " +
            "ORDER BY e.id DESC ")
    List<Event> getAllUsersEvents(List<Long> users, List<Long> catIds, List<EventState> states,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%',:text,'%')) OR :text IS NULL) " +
            "AND ((:categories) is null or e.category.id IN (:categories)) " +
            "AND ((:paid) is null or e.paid = :paid) " +
            "AND e.eventDate > :rangeStart " +
            "AND e.eventDate < :rangeEnd " +
            "order by e.eventDate")
    List<Event> getFilterEventsByDate(String text, List<Long> categories, Boolean paid,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE (UPPER(e.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(e.description) LIKE UPPER(CONCAT('%',:text,'%')) OR :text IS NULL) " +
            "AND ((:categories) is null or e.category.id IN (:categories)) " +
            "AND ((:paid) is null or e.paid = :paid)" +
            "AND e.eventDate > :rangeStart " +
            "AND e.eventDate < :rangeEnd " +
            "order by e.views desc")
    List<Event> getFilterEventsByViews(String text, List<Long> categories, Boolean paid,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);


}


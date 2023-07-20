package ru.main_service.repositories;

import org.springframework.data.jpa.repository.Query;
import ru.main_service.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query(value = "select count(id) " +
            "from Participation " +
            "where event.id = ?1 " +
            "and status = 'CONFIRMED'")
    Integer getConfirmedRequests(Long event);

    List<Participation> findAllByEvent_IdAndEvent_Initiator_Id(Long eventId, Long userId);

    Participation findByRequester_IdAndEvent_Id(Long userId, Long eventId);

    List<Participation> findAllByRequester_Id(Long userId);

    @Query(value = "select p " +
            "from Participation p " +
            "where p.id in (:ids) ")
    List<Participation> findAllParticipation(List<Long> ids);

}

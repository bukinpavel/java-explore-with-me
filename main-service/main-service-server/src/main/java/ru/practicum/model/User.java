package ru.practicum.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.ParticipationRequest;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", schema = "public",
        uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long UserId;
    @Column
    String name;
    @Column
    String email;
    @OneToMany(
            targetEntity = Event.class,
            mappedBy = "initiator",
            fetch = FetchType.LAZY
    )
    List<Event> events;
    @OneToMany(
            targetEntity = ParticipationRequest.class,
            mappedBy = "requester",
            fetch = FetchType.LAZY
    )
    List<ParticipationRequest> requests;


}

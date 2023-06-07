package ru.practicum.model.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.model.User;
import ru.practicum.model.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long requestId;
    @Column
    LocalDateTime created;
    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User requester; // название подумать
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    Event event;
}

package ru.practicum.ewm.dto.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "endpoint_hit")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "app")
    String app;
    @Column(name = "uri")
    String uri;
    @Column(name = "ip")
    String ip;
    @Column(name = "time_stamp")
    LocalDateTime timestamp;
}

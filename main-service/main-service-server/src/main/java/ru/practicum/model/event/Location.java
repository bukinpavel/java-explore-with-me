package ru.practicum.model.event;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    Long locationId;
    @Column(name = "lat")
    Double lat;
    @Column
    Double lon;
    @OneToOne(mappedBy = "location") //проверить работу
    Event event;
}

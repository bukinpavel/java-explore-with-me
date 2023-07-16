package ru.stat_dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class EndpointHitDto {

    @NotNull(message = "App should not be null")
    String app;

    @NotNull(message = "Uri should not be null")
    String uri;

    String ip;

    String timestamp;

}

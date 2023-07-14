package ru.main_service.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class UserDto {

    Long id;

    @NotBlank
    @Email
    @Size(max = 100)
    String email;

    @NotNull
    @Size(min = 3, max = 50)
    String name;

}

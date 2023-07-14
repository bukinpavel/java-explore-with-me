package ru.practicum.mappers;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.model.User;
import ru.practicum.model.dto.UserDto;
import ru.practicum.model.dto.UserNewRequestDto;

@Slf4j
public class UserMapper {

    public static UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        return userDto;
    }


    public static User mapToModel(UserNewRequestDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }
}


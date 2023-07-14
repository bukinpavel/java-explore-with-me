package ru.practicum.services;

import ru.practicum.model.dto.UserDto;
import ru.practicum.model.dto.UserNewRequestDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(List<Long> usersId, int from, int size);

    UserDto addUser(UserNewRequestDto userDto);

    void deleteUser(Long userId);

}


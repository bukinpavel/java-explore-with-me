package ru.main_service.services;

import ru.main_service.model.dto.UserDto;
import ru.main_service.model.dto.UserNewRequestDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers(List<Long> usersId, int from, int size);

    UserDto addUser(UserNewRequestDto userDto);

    void deleteUser(Long userId);

}

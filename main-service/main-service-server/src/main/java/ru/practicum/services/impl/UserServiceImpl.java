package ru.practicum.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.RequestException;
import ru.practicum.mappers.UserMapper;
import ru.practicum.model.User;
import ru.practicum.model.dto.UserDto;
import ru.practicum.model.dto.UserNewRequestDto;
import ru.practicum.repositories.UserRepository;
import ru.practicum.services.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public List<UserDto> getAllUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        if (ids == null) {
            log.info("Запрошен список пользователей с {} в размере {}.", from, size);
            return userRepository.findAll(pageable).stream()
                    .map(UserMapper::mapToDto)
                    .collect(Collectors.toList());
        }
        log.info("Запрошен список пользователей {}.", ids);
        return userRepository.getUsersByIdIsIn(ids, pageable)
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto addUser(UserNewRequestDto userDto) {
        String[] validEmail = userDto.getEmail().split("@");
        if (userDto.getEmail().length() != 254) {
            if (validEmail[0].length() > 64 || validEmail[1].length() > 63)
                throw new RequestException("Недопустимый email");
        }
        User user = userRepository.save(UserMapper.mapToModel(userDto));
        return UserMapper.mapToDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id-" + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

}


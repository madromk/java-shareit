package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InputDataException;
import ru.practicum.shareit.exception.InputExistDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.validate.ValidateUserData;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;
    private final ValidateUserData validateUserData;

    @Autowired
    public UserService(UserStorage userStorage, ValidateUserData validateUserData) {
        this.userStorage = userStorage;
        this.validateUserData = validateUserData;
    }

    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.fromUserDto(userDto);
        if (validateUserData.checkAllData(user)) {
            if (isExistEmail(user.getEmail())) {
                throw new InputExistDataException("Пользователь с таким email уже существует");
            }
            return UserMapper.toUserDto(userStorage.addUser(user));
        } else {
            log.warn("Запрос к эндпоинту POST /users не обработан.");
            throw new ValidationException("Одно или несколько условий не выполняются");
        }
    }

    public UserDto getUser(int id) {
        if (isContainsUser(id)) {
            return UserMapper.toUserDto(userStorage.getUser(id));
        } else {
            log.warn("Запрос к эндпоинту GET /users/{} не обработан", id);
            throw new InputDataException("Пользователь с таким id не найден");
        }
    }

    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(UserDto userDto, int id) {
        User user = UserMapper.fromUserDto(userDto);
        if (!isContainsUser(id)) {
            throw new InputDataException("Пользователь с " + id + " не найден");
        }
        if (user.getEmail() != null && isExistEmail(user.getEmail())) {
            throw new InputExistDataException("Пользователь с таким email уже существует");
        }
        if (id > 0) {
            user.setId(id);
            return UserMapper.toUserDto(userStorage.updateUser(user));
        } else {
            log.warn("Запрос к эндпоинту PATCH /users не обработан.");
            throw new ValidationException("Одно или несколько условий не выполняются");
        }
    }

    public void deleteUser(int id) {
        userStorage.deleteUser(id);
    }

    public boolean isContainsUser(int id) {
        return userStorage.isContainUser(id);
    }

    public boolean isExistEmail(String email) {
        return userStorage.isExistEmail(email);
    }
}

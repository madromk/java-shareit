package ru.practicum.shareit.user.validate;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;

@Slf4j
public class ValidateUserData {

    private final User user;

    public ValidateUserData(User user) {
        this.user = user;
    }

    public boolean checkAllData() {
        if(isCorrectEmail() && isCorrectName()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCorrectEmail() {
        if(user.getEmail() != null && !user.getEmail().isEmpty() && user.getEmail().contains("@")) {
            return true;
        } else {
            log.warn("Ошибка во входных данных. Электронная почта пустая или не содержит @");
            return false;
        }
    }

    public boolean isCorrectName() {
        if(!user.getName().isEmpty() && !user.getName().contains(" ")) {
            return true;
        } else {
            log.warn("Ошибка во входных данных. Логин пустой или содержит пробелы");
            return false;
        }
    }

}

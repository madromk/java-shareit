package ru.practicum.shareit.item.validate;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;

@Slf4j
public class ValidateItemData {

    private final Item item;
    private final int userId;

    public ValidateItemData(Item item, int userId) {
        this.item = item;
        this.userId = userId;
    }

    public boolean checkAllData() {
        if(isCorrectName() && isCorrectDescription() && isCorrectAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCorrectName() {
        if(item.getName() != null && !item.getName().isEmpty()) {
            return true;
        } else {
            log.warn("Ошибка во входных данных. Некорректное название вещи.");
            return false;
        }
    }

    public boolean isCorrectDescription() {
        if(item.getDescription() != null && !item.getDescription().isEmpty()) {
            return true;
        } else {
            log.warn("Ошибка во входных данных. Описание вещи отсутствует или не заполнено");
            return false;
        }
    }

    public boolean isCorrectAvailable() {
        if(item.getAvailable() == null) {
           return false;
        } else {
            return true;
        }
    }
}

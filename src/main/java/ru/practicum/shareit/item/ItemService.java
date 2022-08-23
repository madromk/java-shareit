package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InputDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.validate.ValidateItemData;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Slf4j
public class ItemService {


    private final ItemStorage itemStorage;
    private final UserService userService;

    @Autowired
    public ItemService(ItemStorage itemStorage, UserService userService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
    }


    public Item addItem(Item item, Integer userId) {
        if(userId == null) {
            throw new ValidationException("Отсутствует id пользователя, создавший данную вещь");
        }
        if(!userService.isContainsUser(userId)) {
            throw new InputDataException("Пользователь с id=" + userId + " не найден в БД");
        }
        if(new ValidateItemData(item).checkAllData()) {
            item.setUserId(userId);
            return itemStorage.addItem(item);
        } else {
            throw new ValidationException("Ошибка во входных данных");
        }
    }

    public Item getItemById(int id) {
        if(isContainItem(id)) {
            return itemStorage.getItemById(id);
        } else {
            throw new InputDataException("Вещь по id не найдена");
        }
    }

    public List<Item> getItemsByUserId(int userId) {
            return itemStorage.getItemsByUserId(userId);
    }

    public List<Item> getItemsBySubString(String text) {
        return itemStorage.getItemsBySubString(text);
    }

    public List<Item> getAllItems(Integer userId) {
        if(userId != null) {
            return getItemsByUserId(userId);
        } else {
            return itemStorage.getAllItems();
        }
    }

    public Item updateItem(Item item, Integer userId) {
        if(userId == null) {
            throw new ValidationException("Отсутствует id пользователя, создавший данную вещь");
        }
        Item itemFromDb = getItemById(item.getId());
        if(itemFromDb.getUserId() == userId) {
            return itemStorage.updateItem(item);
        } else {
           throw new InputDataException("Id пользователя не совпадает с id создавшего вещь пользователя");
        }
    }

    public void deleteItem(int id) {
        itemStorage.deleteItem(id);
    }

    public boolean isContainItem(int id) {
        return itemStorage.isContainItem(id);
    }
}

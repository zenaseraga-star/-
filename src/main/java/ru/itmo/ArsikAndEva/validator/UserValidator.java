package ru.itmo.ArsikAndEva.validator;

import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.users.User;
import ru.itmo.ArsikAndEva.users.UserManager;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User user) {
        if(user == null){
            throw new ValidationException("Пользователь не может быть null");
        }
        if(user.getLogin() == null){
            throw new ValidationException("Логин пользователя не должен быть пустым");
        }
        if(user.getUsId() <= 0 ){
            throw new ValidationException("Некорректное ID пользователя");
        }
    }
}

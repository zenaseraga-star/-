package ru.itmo.ArsikAndEva.users;

import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.storage.UserStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class UserManager {
    private final HashMap<Long, User> idUs = new HashMap<>();
    private final HashMap<String, User> logUs = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final UserStorage storage;

    public UserManager(UserStorage storage) {
        this.storage = storage;
        loadUsers();
    }
private void saveUsers(){
    try {
        storage.save(idUs);
    } catch (IOException e) {
        System.out.println("Ошибка при сохранении" + e.getMessage());
    }
}
private void loadUsers(){
        try {
            HashMap<Long, User> users = storage.load();
            for(User us : users.values()){
                idUs.put(us.getUsId(), us);
                logUs.put(us.getLogin(), us);
                if (us.getUsId() >= idCounter.get()) {
                    idCounter.set(us.getUsId() + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке"+ e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Файл не найден");
        }
}
    public User register(String login, String password) {
    if (isLoginExist(login)) {
throw new ValidationException("Имя занято");
    } else {
        long newId = idCounter.getAndIncrement();
        User user = new User(login, Hash.HashPass(password), newId);
        idUs.put(newId, user);
        logUs.put(login, user);
        saveUsers();
        return  user;
    }
}
    public boolean isLoginExist(String login) {
        return logUs.containsKey(login);

    }
    public User login(String login, String password){
    User user = logUs.get(login);
    if(user == null){
        throw new ValidationException("Пользователь не найден");
    }
    if(user.checkPassword(password)) {return user;}
    throw new ValidationException("Неверный пароль");
    }
}



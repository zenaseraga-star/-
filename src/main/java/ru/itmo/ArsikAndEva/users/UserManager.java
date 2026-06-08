package ru.itmo.ArsikAndEva.users;

import ru.itmo.ArsikAndEva.db.UserRepository;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.validator.UserValidator;

import java.sql.SQLException;
import java.util.HashMap;

public class UserManager {
    private final HashMap<Long, User> idUs = new HashMap<>();
    private final HashMap<String, User> logUs = new HashMap<>();
    private final UserRepository repo;
    private final UserValidator userValidator = new UserValidator();

    public UserManager(UserRepository repo) {
        this.repo = repo;
        loadUsers();
    }

    private void loadUsers() {
        try {
            idUs.clear();
            logUs.clear();
            for (User us : repo.findAll()) {
                idUs.put(us.getUsId(), us);
                logUs.put(us.getLogin(), us);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка загрузки пользователей из БД: " + e.getMessage());
        }
    }

        public User register(String login, String password) {
            if (logUs.containsKey(login)) {
                throw new ValidationException("Имя занято");
            }
            try {
                String hash = Hash.HashPass(password);
                long id = repo.insert(login, hash);
                User user = new User(login, hash, id);
                idUs.put(id, user);
                logUs.put(login, user);
                return user;
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) {
                    throw new ValidationException("Имя занято");
                }
                throw new RuntimeException("Ошибка БД при регистрации: " + e.getMessage());
            }
        }

        public boolean isLoginExist(String login){
            return logUs.containsKey(login);
        }

        public User login (String login, String password){
            User user = logUs.get(login);
            if (user == null) {
                throw new ValidationException("Пользователь не найден");
            }

            if (user.checkPassword(password)) {
                return user;
            }
            throw new ValidationException("Неверный пароль");
        }

    public String getLoginById(Long id) {
        if (id == null) return "—";
        User u = idUs.get(id);
        return (u != null) ? u.getLogin() : "—";
    }
}



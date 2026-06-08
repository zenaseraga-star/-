package ru.itmo.ArsikAndEva.users;

import ru.itmo.ArsikAndEva.cli.Command;
import ru.itmo.ArsikAndEva.exception.ValidationException;

import java.util.Scanner;

public class CommandRegister implements Command {
    private final Scanner scanner;
    private final UserManager userManager;
    private final SessionManager sessionManager;

    public CommandRegister(Scanner scanner, UserManager userManager, SessionManager sessionManager) {
        this.scanner = scanner;
        this.userManager = userManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args) {
        String login = null;
        String password = null;
        if(args.length > 2){
    login = args[1];
    password = args[2];
        }
        while (login == null || login.isEmpty()) {
            System.out.println("Введите логин");
            String zn = scanner.nextLine().trim();
            login = zn;
        }

        while (password == null || password.isEmpty()) {
            System.out.println("Введите пароль");
                String zn = scanner.nextLine().trim();
                password = zn;
        }
        try {
       User user = userManager.register(login, password);
        sessionManager.setCurrentUser(user);
        System.out.println("Пользователь зарегистрирован!");
    }
        catch (ValidationException e){
            System.out.println(e.getMessage());
        }
    }
}
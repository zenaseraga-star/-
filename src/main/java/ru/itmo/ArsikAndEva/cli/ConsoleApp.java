package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.storage.FileStorage;
import ru.itmo.ArsikAndEva.storage.UserStorage;
import ru.itmo.ArsikAndEva.users.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {
    private Scanner scanner;
    private final Map<String, Command> commands;

    private final BookingManager bookingManager;
    private final InstrumentManager instrumentManager;
    private final CheckoutManager checkoutManager;
    private final UserManager userManager;
    private final FileStorage storage;
    private final SessionManager sessionManager;


    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        this.instrumentManager = new InstrumentManager();
        this.bookingManager = new BookingManager(instrumentManager);
        this.checkoutManager = new CheckoutManager(instrumentManager);
        this.storage = new FileStorage("data.ser");
        this.sessionManager = new SessionManager();
        this.userManager = new UserManager(new UserStorage("users.ser"));


        commands = new HashMap<>();
        commands.put("checkout_take", new CheckoutTakeCommand(instrumentManager, checkoutManager, scanner, sessionManager));
        commands.put("book_create", new BookCreatCommand(instrumentManager, bookingManager, scanner, sessionManager));
        commands.put("book_list", new BookListCommand(bookingManager, scanner));
        commands.put("book_reschedule", new BookRescheduleCommand(bookingManager, scanner,sessionManager));
        commands.put("book_cancel", new BookCancelCommand(bookingManager,sessionManager));
        commands.put("book_show", new BookShowCommand(bookingManager));
        commands.put("checkout_return", new CheckoutReturnCommand(instrumentManager, checkoutManager, scanner, sessionManager));
        commands.put("checkout_list", new CheckoutListCommand(checkoutManager));
        commands.put("inst_available", new InstAvailableCommand(instrumentManager, bookingManager));
        commands.put("checkout_show", new CheckoutShowCommand(checkoutManager));
        commands.put("inst_add", new InstAddCommand(instrumentManager,sessionManager));
        commands.put("help", new HelpCommand());
        commands.put("save", new SaveCommand(scanner, bookingManager, instrumentManager,checkoutManager, sessionManager, storage ));
        commands.put("load", new LoadCommand(scanner, bookingManager, checkoutManager, instrumentManager));
        commands.put("login", new CommandLogin(scanner, userManager, sessionManager));
        commands.put("register", new CommandRegister(scanner, userManager, sessionManager ));
        commands.put("logout", new ExitUserCommand(sessionManager));
    }



    public void start(){
        System.out.println("Добро пожаловать в наш проект ArsikAndEva!!");
        System.out.println("Чтобы ознакомиться со списком команд напишите 'help'");
        boolean isRunnable = true;

        while (isRunnable){
            System.out.print("Введите команду: ");
            if (!scanner.hasNext()) break;

            try {
                String input = scanner.nextLine();

                if (input.isBlank()){
                    System.out.println("Введите команду!");
                    continue;
                }

                String[] args = input.trim().split(" ");

                String cmd = args[0];

                if (cmd.equals("exit")){
                    isRunnable = false;
                    continue;
                }


                Command command = commands.get(cmd);
                if (command == null){
                    System.out.println("Команда " + args[0] + " не найдена. Чтобы ознакомиться со списком команд введите help");
                    continue;
                }
                command.execute(args);
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
        }
        System.out.println("Работа завершена, пока!");
    }
}

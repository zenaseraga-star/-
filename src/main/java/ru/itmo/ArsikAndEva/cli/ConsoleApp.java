package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleApp {
    private Scanner scanner;
    private final Map<String, Command> commands;

    private final BookingManager bookingManager;
    private final InstrumentManager instrumentManager;
    private final CheckoutManager checkoutManager;


    public ConsoleApp() {
        this.scanner = new Scanner(System.in);
        this.instrumentManager = new InstrumentManager();
        this.bookingManager = new BookingManager(instrumentManager);
        this.checkoutManager = new CheckoutManager(instrumentManager);


        commands = new HashMap<>();
        commands.put("checkout_take", new CheckoutTakeCommand(instrumentManager, checkoutManager, scanner));
        commands.put("book_create", new BookCreatCommand(bookingManager, scanner));
        commands.put("book_list", new BookListCommand(bookingManager));
        commands.put("book_reschedule", new BookRescheduleCommand(bookingManager));
        commands.put("book_cancel", new BookCancelCommand(bookingManager));
        commands.put("book_show", new BookShowCommand(bookingManager));
        commands.put("checkout_return", new CheckoutReturnCommand(checkoutManager, scanner));
        commands.put("checkout_list", new CheckoutListCommand(checkoutManager));
        commands.put("inst_available", new InstAvailableCommand(instrumentManager, bookingManager));
        commands.put("checkout_show", new CheckoutShowCommand(checkoutManager));
        commands.put("inst_add", new InstAddCommand(instrumentManager));
        commands.put("help", new HelpCommand());
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

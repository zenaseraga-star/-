package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.users.SessionManager;


public class BookCancelCommand implements Command {
    private BookingManager bookingManager;
    private final SessionManager sessionManager;


    public BookCancelCommand(BookingManager bookingManager, SessionManager sessionManager) {
        this.bookingManager = bookingManager;
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args) {
        if(!sessionManager.isExistUser()){
            System.out.println("У вас нет прав для этой команды");
            return;
        }
        try {
            if (args.length < 2) {
                throw new ValidationException(" Введите id ");
            }
            long bookId = Long.parseLong(args[1]);
            bookingManager.bookCancel(bookId);
            System.out.println(" OK canceled ");
        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
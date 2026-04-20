package main.java.ru.itmo.ArsikAndEva.cli;

import main.java.ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import main.java.ru.itmo.ArsikAndEva.manager.BookingManager;

import java.util.Scanner;


public class BookCancelCommand implements Command {
    private BookingManager bookingManager;


    public BookCancelCommand(BookingManager bookingManager) {
        this.bookingManager = bookingManager;

    }

    @Override
    public void execute(String[] args) {
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
package main.java.ru.itmo.ArsikAndEva.cli;


import main.java.ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import  main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import  main.java.ru.itmo.ArsikAndEva.manager.BookingManager;

import java.util.Scanner;

public class BookShowCommand implements Command {
    BookingManager bookingManager;


    public BookShowCommand(BookingManager bookingManager) {
        this.bookingManager = bookingManager;

    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 2) {
                throw new ValidationException(" Введите Id ");
            }
            long bookId = Long.parseLong(args[1]);

            System.out.println(bookingManager.getBookById(bookId));
        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
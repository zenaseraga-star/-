package ru.itmo.ArsikAndEva.cli;


import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import  ru.itmo.ArsikAndEva.exception.ValidationException;
import  ru.itmo.ArsikAndEva.manager.BookingManager;

import java.util.Scanner;

public class BookShowCommand implements Command {
    BookingManager bookingManager;
    Scanner scanner;

    public BookShowCommand(BookingManager bookingManager, Scanner scanner) {
        this.bookingManager = bookingManager;

        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 1) {
                throw new ValidationException(" Введите Id ");
            }
            long bookId = Long.parseLong(args[0]);

            System.out.println(bookingManager.getBookById(bookId));
        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

package ru.itmo.ArsikAndEva.cli;



import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.model.Booking;

import java.util.Scanner;

public class BookCreatCommand implements Command {
    private BookingManager bookingManager;
    private Scanner scanner;

    public BookCreatCommand(BookingManager bookingManager, Scanner scanner) {
        this.bookingManager = bookingManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 1) {
                throw new ValidationException(" Введите id инструмента");
            }
            long instrumentId = Long.parseLong(args[0]);
            System.out.println(" Начало (YYYY-MM-DD HH:MM): ");
            String startAt = scanner.nextLine();
            System.out.println(" Конец  (YYYY-MM-DD HH:MM): ");
            String endAt = scanner.nextLine();
            long bookId = bookingManager.createBook(instrumentId, startAt, endAt, "System");
            System.out.println(" OK book_id =" + bookId);
        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.util.Scanner;

public class BookCreatCommand implements Command {
    private InstrumentManager instrumentManager;
    private BookingManager bookingManager;
    private Scanner scanner;

    public BookCreatCommand(InstrumentManager instrumentManager, BookingManager bookingManager, Scanner scanner)  {
        this.instrumentManager = instrumentManager;
        this.bookingManager = bookingManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 2) {
                throw new ValidationException(" Введите id инструмента");
            }
            long instrumentId = Long.parseLong(args[1]);

            instrumentManager.getById(instrumentId).orElseThrow(() -> new EntityNotFoundException("Инструмента с таким ID нету!"));

            String startAt = null;
            System.out.print("Начало (YYYY-MM-DD HH:MM): ");

            while(startAt == null){

                try{
                    String zn = scanner.nextLine().trim();
                    BookingValidator.validateTime(zn);
                    startAt = zn;

                }
                catch (IllegalArgumentException | ValidationException e ){
                    System.out.println("Введите время начала правильно!");
                }

            }
            String endAt = null;
            System.out.println("Конец  (YYYY-MM-DD HH:MM): ");
            while (endAt == null){
                try {
                    String zn = scanner.nextLine().trim();
                    BookingValidator.validateTime(zn);
                    endAt = zn;
                }
                catch (IllegalArgumentException | ValidationException e ){
                    System.out.println("Введите время конца правильно!");
                }
            }
            long bookId = bookingManager.createBook(instrumentId, startAt, endAt, "System");
            System.out.println(" OK book_id =" + bookId);

        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
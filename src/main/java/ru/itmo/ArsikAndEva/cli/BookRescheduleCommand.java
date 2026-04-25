package ru.itmo.ArsikAndEva.cli;


import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.util.Scanner;


public class BookRescheduleCommand implements Command {
    BookingManager bookingManager;
    private Scanner scanner;
    public BookRescheduleCommand(BookingManager bookingManager, Scanner scanner){
        this.bookingManager = bookingManager;
        this.scanner = scanner;
    }
    @Override
    public void execute(String[] args){
        try{
            if(args.length < 2){
                throw new ValidationException( " Введите ID");
            }
            long bookId = Long.parseLong(args[1]);
            System.out.println("Введите время начала перенесенной брони [--from YYYY-MM-DD]");
            String startAt = null;
            while(startAt == null){

                try{
                    String zn = scanner.nextLine().trim().toUpperCase();
                    BookingValidator.validateTime(zn);
                    startAt = zn;

                }
                catch (IllegalArgumentException | ValidationException e ){
                    System.out.println("Введите время начала правильно!");
                }

            }
            System.out.println("Введите время конца перенесенной брони[--from YYYY-MM-DD]");
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
            bookingManager.bookReschedule(bookId, startAt, endAt);
            System.out.println(" OK Бронь перенесена ");
        }
        catch  (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}
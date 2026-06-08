package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.util.Scanner;

public class BookCreatCommand implements Command {
    private InstrumentManager instrumentManager;
    private BookingManager bookingManager;
    private Scanner scanner;
    private final SessionManager sessionManager;

    public BookCreatCommand(InstrumentManager instrumentManager, BookingManager bookingManager, Scanner scanner, SessionManager sessionManager)  {
        this.instrumentManager = instrumentManager;
        this.bookingManager = bookingManager;
        this.scanner = scanner;
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
                    String zn = scanner.nextLine().trim().toUpperCase();
                    BookingValidator.validateTime(zn);
                    endAt = zn;
                }
                catch (IllegalArgumentException | ValidationException e ){
                    System.out.println("Введите время конца правильно!");
                }
            }
            String ownerName = null;
            while (ownerName==null){
                try{
                    ownerName = scanner.nextLine().trim();
                }
                catch (IllegalArgumentException e){
                    System.out.println("Введите имя правильно");
                }
            }
            long bookId = bookingManager.createBook(instrumentId, startAt, endAt, sessionManager.getCurrentUser().getUsId(), ownerName);
            System.out.println(" OK book_id =" + bookId);

        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
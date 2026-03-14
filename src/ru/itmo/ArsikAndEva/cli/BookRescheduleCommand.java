package ru.itmo.ArsikAndEva.cli;


import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.validator.BookingValidator;


public class BookRescheduleCommand implements Command {
    BookingManager bookingManager;
    Scanner scanner;
    public BookRescheduleCommand(BookingManager bookingManager){
        this.bookingManager = bookingManager;
    }
    @Override
    public void execute(String[] args){
        try{
            if(args.length != 4){
                throw new ValidationException( " Введите ID, время начала, время конца ");
            }
            long bookId = Long.parseLong(args[1]);
            BookingValidator.validateTime(args[2]);
            BookingValidator.validateTime(args[3]);
            bookingManager.bookReschedule(bookId, args[2], args[3]);
            System.out.println(" OK Бронь перенесена ");
        }
        catch  (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
    }
}

package main.java.ru.itmo.ArsikAndEva.cli;



import main.java.ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import main.java.ru.itmo.ArsikAndEva.manager.BookingManager;
import main.java.ru.itmo.ArsikAndEva.model.Booking;
import main.java.ru.itmo.ArsikAndEva.validator.BookingValidator;
import java.util.List;
import java.util.Scanner;


public class BookListCommand implements Command {
    BookingManager bookingManager;
     private Scanner scanner;

    public BookListCommand(BookingManager bookingManager, Scanner scanner){
        this.bookingManager = bookingManager;
        this.scanner = scanner;
    }
    @Override
    public void execute(String[] args){
        try{
            if(args.length < 2){
                throw new ValidationException( " book_list <instrument_id>");
            }
            long instrumentId = Long.parseLong(args[1]);
            System.out.println("Введите дату, с которого вывести список[--from YYYY-MM-DD]");
            String startAt = null;
            while(startAt == null){

                try{
                    String zn = scanner.nextLine().trim();
                    BookingValidator.validateTime(zn);
                    startAt = zn;

                }
                catch (IllegalArgumentException | ValidationException e ){
                    System.out.println("Введите время правильно!");
                }

            }
            List<Booking> bookings = bookingManager.booklist(instrumentId, startAt );

            for(Booking booking : bookings){
                System.out.println(booking);
            }
        } catch (NumberFormatException e) {
            System.out.println(" ID должен быть числом ");
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
}

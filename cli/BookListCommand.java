package ru.itmo.ArsikAndEva.cli;



import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.validator.BookingValidator;
import java.util.List;


public class BookListCommand implements Command {
    BookingManager bookingManager;

    public BookListCommand(BookingManager bookingManager){
        this.bookingManager = bookingManager;
    }
    @Override
    public void execute(String[] args){
        try{
            if(args.length < 3){
                throw new ValidationException( " book_list <instrument_id> [--from YYYY-MM-DD]");
            }
            long instrumentId = Long.parseLong(args[0]);
            BookingValidator.validateTime(args[2]);

            List<Booking> bookings = bookingManager.booklist(instrumentId, args[2]);

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

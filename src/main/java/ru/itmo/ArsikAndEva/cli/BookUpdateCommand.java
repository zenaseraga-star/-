//package ru.itmo.ArsikAndEva.cli;
//
//import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
//import ru.itmo.ArsikAndEva.exception.ValidationException;
//import ru.itmo.ArsikAndEva.manager.BookingManager;
//import ru.itmo.ArsikAndEva.model.Booking;
//import ru.itmo.ArsikAndEva.validator.BookingValidator;
//
//import java.util.Optional;
//import java.util.Scanner;
//
//public class BookUpdateCommand implements Command {
//    private BookingManager bookingManager;
//    private Scanner scanner;
//
//    public BookUpdateCommand(BookingManager bookingManager, Scanner scanner) {
//        this.bookingManager = bookingManager;
//        this.scanner = scanner;
//    }
//
//    @Override
//    public void execute(String[] args) {
//        try {
//            if (args.length < 2) {
//                throw new ValidationException(" введите id ");
//            }
//            long bookId;
//            try {bookId = Long.parseLong(args[1]);
//            } catch (ValidationException | EntityNotFoundException e) {
//                System.out.println(e.getMessage());
//                return;
//            }
//            System.out.println("Новое начало (YYYY-MM-DD HH:MM): ");
//            String startAt = scanner.nextLine();
//            Optional<String> newStart = startAt.isEmpty()? Optional.empty() : Optional.of(startAt);
//            if(newStart.isPresent()) {
//                try {
//                    BookingValidator.validateTime(newStart.get());
//                } catch (ValidationException e) {
//                    System.out.println(e.getMessage());
//                    return;
//                }
//            }
//            System.out.println(" Конец  (YYYY-MM-DD HH:MM): ");
//            String endAt = scanner.nextLine();
//            Optional<String> newEnd = endAt.isEmpty()? Optional.empty() : Optional.of(endAt);
//            if(newEnd.isPresent()){
//                try {
//                    BookingValidator.validateTime(newEnd.get());
//                }
//                catch (ValidationException e){
//                    System.out.println(e.getMessage());
//                }
//            }
//            if(newStart.isPresent() && newEnd.isPresent()){
//
//            }
//            Booking booking = bookingManager.updateBooking(bookId, startAt, endAt, "System");
//            System.out.println(" OK book_id =" + bookId);
//        } catch (NumberFormatException e) {
//            System.out.println(" ID должен быть числом ");
//        } catch (ValidationException | EntityNotFoundException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}
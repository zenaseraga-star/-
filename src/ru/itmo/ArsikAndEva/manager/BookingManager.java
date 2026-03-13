package ru.itmo.ArsikAndEva.manager;


import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import  ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.Instrument;

import java.time.ZoneOffset;
import java.time.format.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.enums.BookingStatus;
import java.time.Instant;
import ru.itmo.ArsikAndEva.validator.BookingValidator;
import ru.itmo.ArsikAndEva.validator.InstrumentValidator;

import static java.time.Instant.parse;

public class BookingManager {
    private Map<Long, Booking> books = new HashMap<>();

    private BookingValidator BookingValidator = new BookingValidator();
    public long createBook(long instrumentId, String startAt, String endAt, String owner){
        InstrumentManager im = new InstrumentManager();
        Instant start =  Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(startAt));
        Instant end =  Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(endAt));
        if(end.isBefore(start)){
            throw new ValidationException(" Конец не может быть раньше начала");
        }
        Instrument in = im.getById(instrumentId) ;
       List<Booking> byInstrument = findByInId(instrumentId);
       for(Booking booking : byInstrument){
           
       
        
        if(booking != null && booking.getStatus() == BookingStatus.ACTIVE && (booking.getEndAt().isAfter(start) || booking.getStartAt().isBefore(end)) ){
            throw new ValidationException(" Этот инструмент уже забронирован на это время");
        }}
        
        long millis = Instant.now().getEpochSecond();
        Booking book = new Booking(millis+ books.size(), Instant.now());
        book.setStartAt(start);
        book.setEndAt(end);
        book.setInstrumentId(instrumentId);
        book.setOwnerUsername(owner);
        book.setStatus(BookingStatus.ACTIVE);
        books.put(book.getId(), book);
        BookingValidator.validate(book);
        return book.getId();

    }
public List<Booking> booklist (String date) {
    
    if (date == null) {
        throw new ValidationException(" Неверный формат даты");
    } else {

        Instant dat = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(date));

        List<Booking> res = new ArrayList<>();
        for (Booking book : books.values()) {
            if (book.getStatus() == BookingStatus.ACTIVE && book.getCreatedAt().isAfter(dat)) {
                res.add(book);
            }
        }


        return res;
    }}
public void bookCancel(long bookId){
        Booking book = getBookById(bookId);
        
        
            if( Instant.now().isBefore(book.getStartAt())){
               book.setStatus(BookingStatus.CANCELLED);

            }
            else{
    throw new ValidationException(" Нельзя отменить начавшуюся бронь");}
        }


public List<Booking> findByInId(long instrumentId){
        List<Booking> list = new ArrayList<>();
        for(Booking book : books.values()){
            if(book.getInstrumentId() == instrumentId){
                list.add(book);
            }
        }
        return list;
}
public void bookReschedule(long bookId, String startAt, String endAt){
    Instant start  = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(startAt));
    Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(endAt));
  
    if(start.isAfter(end)){
        throw new ValidationException(" Конец не может быть раньше начала ");
    }
    Booking book = getBookById(bookId);
    book.setStartAt(start);
    book.setEndAt(end);
}
public Booking getBookById(long bookId){
    if (!books.containsKey(bookId))
        throw new EntityNotFoundException(" Брони с id " + bookId + " нет в списке");
    return books.get(bookId);
}
public Booking updateBooking(long Id,Optional <String> startAt, Optional<String> endAt,Optional <String>owner) {
    InstrumentManager im = new InstrumentManager();
    Optional<Instant> start = startAt.map(s -> Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(s)));
    Optional<Instant> end = endAt.map(s -> Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(s)));

    Booking book = getBookById(Id);
    BookingValidator.validate(book);
    Instant newStartAt = start.orElse(book.getStartAt());
    Instant newEndAt = end.orElse(book.getEndAt());
    String newOwner = owner.orElse(book.getOwnerUsername());
    book.setStartAt(newStartAt);
    book.setEndAt(newEndAt);
    book.setOwnerUsername(newOwner);
    BookingValidator.validate(book);
return book;
}
public void removeBooking(long bookId){
        Booking book = getBookById(bookId);
        books.remove(bookId, book);
}
}

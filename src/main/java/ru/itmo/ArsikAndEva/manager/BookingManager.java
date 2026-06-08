package ru.itmo.ArsikAndEva.manager;

import ru.itmo.ArsikAndEva.db.BookingRepository;
import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.enums.BookingStatus;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookingManager {
    private final Map<Long, Booking> books = new HashMap<>();
    private InstrumentManager instrumentManager;
    private final BookingRepository repo;
    private final BookingValidator bookingValidator = new BookingValidator();

    public BookingManager(InstrumentManager instrumentManager, BookingRepository repo) {
        this.instrumentManager = instrumentManager;
        this.repo = repo;
        loadAll();
    }

    public void loadAll(){
        try{
            books.clear();
            for (Booking b : repo.findAll())
                books.put(b.getId(), b);
            } catch (SQLException e){
            System.out.println("Ошибка загрузки броней из БД: " + e.getMessage());
        }
    }

    public long createBook(long instrumentId, String startAt, String endAt, Long owner) {
        instrumentManager.getById(instrumentId).orElseThrow(() -> new EntityNotFoundException("Инструмент с таким id не найден"));

        Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(startAt));
        Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(endAt));
        if (end.isBefore(start)) {
            throw new ValidationException("Конец не может быть раньше начала");
        }

        List<Booking> byInstrument = findByInId(instrumentId);
        for (Booking booking : byInstrument) {
            if (booking != null && booking.getStatus() == BookingStatus.ACTIVE && !((start.isBefore(booking.getStartAt()) && end.isBefore(booking.getStartAt()) || start.isAfter(booking.getEndAt())))) {
                throw new ValidationException("Этот инструмент уже забронирован на это время");
            }
        }

        Instant now = Instant.now();
        Booking book = new Booking(0, instrumentId, start, end, BookingStatus.ACTIVE, owner, now, now);
        bookingValidator.validate(book);

        try{
            long id = repo.insert(book);
            books.put(id, book);
            return id;
        } catch (SQLException e) {
            if (e.getSQLState().equals("23503"))
                throw new ValidationException("Прибор не найден в базе");
            throw new RuntimeException("Ошибка БД при создании брони: " + e.getMessage(), e);
        }
    }

    public List<Booking> booklist(long instrumentId, String date) {
        if (date == null) {
            throw new ValidationException("Неверный формат даты");
        } else {
            Instant dat = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(date));

            List<Booking> res = new ArrayList<>();
            for (Booking book : books.values()) {
                if (book.getStatus() == BookingStatus.ACTIVE && book.getCreatedAt().isAfter(dat)) {
                    res.add(book);
                }
            }
            return res;
        }
    }

    public void bookCancel(long bookId) {
        Booking book = getBookById(bookId);
        if (Instant.now().isBefore(book.getStartAt())) {
            book.setStatus(BookingStatus.CANCELLED);
            saveToDb(book);
        } else {
            throw new ValidationException("Нельзя отменить начавшуюся бронь");
        }
    }

    public List<Booking> findByInId(long instrumentId) {
        List<Booking> list = new ArrayList<>();
        for (Booking book : books.values()) {
            if (book.getInstrumentId() == instrumentId) {
                list.add(book);
            }
        }
        return list;
    }

    public void bookReschedule(long bookId, String startAt, String endAt) {
        Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(startAt));
        Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(endAt));
        if (start.isAfter(end)) {
            throw new ValidationException(" Конец не может быть раньше начала ");
        }
        Booking book = getBookById(bookId);
        book.setStartAt(start);
        book.setEndAt(end);
        saveToDb(book);
    }

    public Booking getBookById(long bookId) {
        if (!books.containsKey(bookId))
            throw new EntityNotFoundException("Брони с id " + bookId + " нет в списке");
        return books.get(bookId);
    }

    public Booking updateBooking(long Id, String startAt, String endAt, Long owner) {
        Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(startAt));
        Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(endAt));
        Booking book = getBookById(Id);
        bookingValidator.validate(book);
        book.setStartAt(start);
        book.setEndAt(end);
        book.setOwnerId(owner);
        bookingValidator.validate(book);
        return book;
    }

    public void removeBooking(long bookId) {
        getBookById(bookId);
        try{
            repo.delete(bookId);
            books.remove(bookId);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка БД при сохранении брони: " + e.getMessage(), e);
        }
    }

    private void saveToDb(Booking book){
        try{
            repo.update(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Booking> getAll() {
        return new ArrayList<>(books.values());
    }

//    public  HashMap<Long, Booking> getData(){
//        return new HashMap<>(books);
//    }
//
//    public void loadData(HashMap<Long, Booking> newBookings){
//        this.books.clear();
//        this.books.putAll(newBookings);
//    }

}
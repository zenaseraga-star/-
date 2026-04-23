package ru.itmo.ArsikAndEva.manager;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.enums.BookingStatus;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

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

    public BookingManager(InstrumentManager instrumentManager) {
        this.instrumentManager = instrumentManager;
    }

    private BookingValidator BookingValidator = new BookingValidator();

    public long createBook(long instrumentId, String startAt, String endAt, String owner) {
        instrumentManager.getById(instrumentId).orElseThrow(() -> new EntityNotFoundException("Инструмент с таким id не найден"));


        Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(startAt));
        Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(endAt));
        if (end.isBefore(start)) {
            throw new ValidationException("Конец не может быть раньше начала");
        }

        List<Booking> byInstrument = findByInId(instrumentId);
        for (Booking booking : byInstrument) {
            if (booking != null && booking.getStatus() == BookingStatus.ACTIVE && (booking.getEndAt().isAfter(start) || booking.getStartAt().isBefore(end))) {
                throw new ValidationException("Этот инструмент уже забронирован на это время");
            }
        }

        long millis = Instant.now().getEpochSecond();
        Booking book = new Booking(millis + books.size(), Instant.now());
        book.setStartAt(start);
        book.setEndAt(end);
        book.setInstrumentId(instrumentId);
        book.setOwnerUsername(owner);
        book.setStatus(BookingStatus.ACTIVE);
        books.put(book.getId(), book);
        BookingValidator.validate(book);
        return book.getId();

    }

    public List<Booking> booklist(long instrumentId, String date) {
        if (date == null) {
            throw new ValidationException(" Неверный формат даты");
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
        } else {
            throw new ValidationException(" Нельзя отменить начавшуюся бронь");
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
    }

    public Booking getBookById(long bookId) {
        if (!books.containsKey(bookId))
            throw new EntityNotFoundException(" Брони с id " + bookId + " нет в списке");
        return books.get(bookId);
    }

    public Booking updateBooking(long Id, String startAt, String endAt, String owner) {
        Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(startAt));
        Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(endAt));
        Booking book = getBookById(Id);
        BookingValidator.validate(book);
        book.setStartAt(start);
        book.setEndAt(end);
        book.setOwnerUsername(owner);
        BookingValidator.validate(book);
        return book;
    }

    public void removeBooking(long bookId) {
        Booking book = getBookById(bookId);
        books.remove(bookId, book);
    }

    public List<Booking> getAll() {
        return new ArrayList<>(books.values());
    }


    public  HashMap<Long, Booking> getData(){
        return new HashMap<>(books);
    }
    public void loadData(HashMap<Long, Booking> newBookings){
        this.books.clear();
        this.books.putAll(newBookings);
    }

    }
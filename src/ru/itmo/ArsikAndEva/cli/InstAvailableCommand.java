package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;

import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class InstAvailableCommand implements Command {
    private final InstrumentManager instrumentManager;
    private final BookingManager bookingManager;

    public InstAvailableCommand(InstrumentManager instrumentManager,  BookingManager bookingManager) {
        this.instrumentManager = instrumentManager;
        this.bookingManager = bookingManager;
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 4)
                throw new ValidationException("Нужно ввести type, start, end");

            InstrumentType instrumentType = InstrumentType.valueOf(args[1].toUpperCase());

            List<Instrument> instrumentList = instrumentManager.getAll();
            List<Booking> bookingList = bookingManager.getAll();

            Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(args[2]));
            Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC).parse(args[3]));

            List<Instrument> availableInstList = instrumentList.stream().filter(inst -> inst.getStatus() == InstrumentStatus.ACTIVE && inst.getType() == instrumentType).filter(inst ->
                    bookingList.stream().noneMatch(booking -> {
                        boolean isSameInstrument = booking.getInstrumentId() == inst.getId();
                        boolean isOverlapping = booking.getStartAt().isBefore(end) && booking.getEndAt().isAfter(start);
                        return isSameInstrument && isOverlapping;
                    })).toList();


            if (availableInstList.isEmpty()) {
                System.out.println("Нет доступных приборов");
                return;
            }

            availableInstList.forEach(System.out::println);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Неизвестный тип прибора. Доступные типы: ...");
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка в формате времени! Используйте формат yyyy-MM-dd HH:mm");
        }
    }
}

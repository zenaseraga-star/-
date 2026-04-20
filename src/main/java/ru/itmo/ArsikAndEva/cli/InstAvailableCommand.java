package main.java.ru.itmo.ArsikAndEva.cli;

import main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import main.java.ru.itmo.ArsikAndEva.manager.BookingManager;

import main.java.ru.itmo.ArsikAndEva.manager.InstrumentManager;
import main.java.ru.itmo.ArsikAndEva.model.Booking;
import main.java.ru.itmo.ArsikAndEva.model.Instrument;
import main.java.ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import main.java.ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class InstAvailableCommand implements Command {
    private final Scanner scanner;
    private final InstrumentManager instrumentManager;
    private final BookingManager bookingManager;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC);

    public InstAvailableCommand(InstrumentManager instrumentManager,  BookingManager bookingManager) {
        this.instrumentManager = instrumentManager;
        this.bookingManager = bookingManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void execute(String[] args) {
        try {
            InstrumentType instrumentType = null;
            while (instrumentType == null) {
                System.out.println("Актуальный список типов поддерживаемых инструментов: ");
                Arrays.stream(InstrumentType.values()).forEach(type -> System.out.println("    " + type));

                System.out.print("Введите тип инструмента: ");
                try {
                    instrumentType = InstrumentType.valueOf(scanner.nextLine().trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Неправильный тип инструмента, попробуйте еще раз!");
                }
            }

            List<Instrument> instrumentList = instrumentManager.getAll();
            List<Booking> bookingList = bookingManager.getAll();

            Instant start = readInstant("Введите начальное время (yyyy-MM-dd HH:mm): ");
            Instant end = readInstant("Введите время окончания (yyyy-MM-dd HH:mm): ");


            Set<Long> occupiedInstrumentIds = bookingList.stream()
                    .filter(booking -> isOverlapping(booking, start, end))
                    .map(Booking::getInstrumentId)
                    .collect(Collectors.toSet());

            InstrumentType finalInstrumentType = instrumentType;
            List<Instrument> availableInstList = instrumentList.stream()
                    .filter(inst -> inst.getStatus() == InstrumentStatus.ACTIVE)
                    .filter(inst -> inst.getType() == finalInstrumentType)
                    .filter(inst -> !occupiedInstrumentIds.contains(inst.getId()))
                    .toList();

            if (availableInstList.isEmpty()) {
                System.out.println("Нет доступных приборов");
                return;
            }
            System.out.println("\nСписок доступных приборов на данное время: ");
            availableInstList.forEach(inst -> System.out.println("    " + inst));
            System.out.println();
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isOverlapping(Booking booking, Instant start, Instant end) {
        return booking.getStartAt().isBefore(end) && booking.getEndAt().isAfter(start);
    }

    private Instant readInstant(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Instant.from(DATE_FORMATTER.parse(scanner.nextLine().trim()));
            } catch (DateTimeParseException e) {
                System.out.println("Неправильный формат времени, попробуйте еще раз!");
            }
        }
    }
}

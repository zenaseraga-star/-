package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.cli.ConsoleApp;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.util.Scanner;

public class InstAddCommand implements Command{
    private final Scanner scanner;
    private final InstrumentManager instrumentManager;

    public InstAddCommand(InstrumentManager instrumentManager, Scanner scanner) {
        this.scanner = new Scanner(System.in);
        this.instrumentManager = instrumentManager;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Введите имя для прибора: ");
        String name = scanner.nextLine().trim();



        System.out.println("Введите тип прибора: ");
        InstrumentType instrumentType = InstrumentType.valueOf(scanner.nextLine().trim().toUpperCase());


        System.out.println("Введите inventoryNumber");
        String inventoryNumber = scanner.nextLine().trim();

        System.out.println("Напишите где находится прибор");
        String location = scanner.nextLine().trim();

        System.out.println("Введите статус прибора (ACTIVE или OUT_OF_SERVICE): ");
        InstrumentStatus instrumentStatus = InstrumentStatus.valueOf(scanner.nextLine().toUpperCase());

        Instrument instrument = new Instrument(name, instrumentType, inventoryNumber, location, instrumentStatus);

        instrumentManager.add(instrument);
    }
}

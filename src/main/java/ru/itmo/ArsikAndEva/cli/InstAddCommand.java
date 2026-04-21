package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.util.Scanner;

public class InstAddCommand implements Command{
    private final Scanner scanner;
    private final InstrumentManager instrumentManager;

    public InstAddCommand(InstrumentManager instrumentManager) {
        this.scanner = new Scanner(System.in);
        this.instrumentManager = instrumentManager;
    }

    @Override
    public void execute(String[] args) {
        String name = "";
        while (name.isBlank()){
            System.out.print("Введите имя прибора: ");
            name = scanner.nextLine().trim();
        }


        InstrumentType instrumentType = null;
        while (instrumentType == null) {
            System.out.println("Список типов имеющихся приборов: ");
            for (InstrumentType type: InstrumentType.values()) {
                System.out.println("    " + type);
            }
            try {
                System.out.print("Введите тип прибора: ");
                instrumentType = InstrumentType.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Таких приборов нет! Попробуйте еще раз!");
            }
        }

        System.out.print("Введите inventoryNumber: ");
        String inventoryNumber = scanner.nextLine().trim();

        System.out.print("Напишите где находится прибор: ");
        String location = scanner.nextLine().trim();


        InstrumentStatus instrumentStatus = null;
        while (instrumentStatus == null){
            System.out.print("Введите статус прибора (ACTIVE или OUT_OF_SERVICE): ");
            try {
                instrumentStatus = InstrumentStatus.valueOf(scanner.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Неправильный формат статуса, попробуйте еще раз!");
            }
        }

        Instrument instrument = new Instrument(name, instrumentType, inventoryNumber, location, instrumentStatus);
        instrumentManager.add(instrument);

        System.out.println("Инструмент добавлен! Ура-ура! Вперед-вперед! Id инструмента: " + instrument.getId());
    }
}

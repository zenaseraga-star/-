package main.java.ru.itmo.ArsikAndEva.cli;

import main.java.ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import main.java.ru.itmo.ArsikAndEva.manager.CheckoutManager;

import main.java.ru.itmo.ArsikAndEva.manager.InstrumentManager;
import main.java.ru.itmo.ArsikAndEva.model.Instrument;
import main.java.ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import main.java.ru.itmo.ArsikAndEva.model.enums.ReturnCondition;


import java.util.Scanner;

public class CheckoutReturnCommand implements Command{
    private final CheckoutManager checkoutManager;
    private final Scanner scanner;
    private final InstrumentManager instrumentManager;

    public CheckoutReturnCommand(InstrumentManager instrumentManager, CheckoutManager checkoutManager, Scanner scanner) {
        this.checkoutManager = checkoutManager;
        this.instrumentManager = instrumentManager;
        this.scanner = scanner;
    }


    @Override
    public void execute(String[] args) {
        try {
            Instrument instrument = null;
            while (instrument == null) {
                System.out.print("Введите id прибора: ");
                long checkoutId = Long.parseLong(scanner.nextLine());
                instrument = instrumentManager.getById(checkoutId).orElse(null);

                if (instrument != null && instrument.getStatus() == InstrumentStatus.ACTIVE) {
                    System.out.println("Прибор уже на складе. Введите ID другого прибора.");
                    instrument = null;
                }
            }

            ReturnCondition condition = null;
            while (condition == null) {
                System.out.print("Введите состояние (OK, DAMAGED): ");
                String input = scanner.nextLine().trim().toUpperCase();
                try {
                    condition = ReturnCondition.valueOf(input);
                } catch (IllegalArgumentException e) {
                    System.out.println("Ошибка! Можно вводить только: ");
                    for (ReturnCondition rc : ReturnCondition.values()) System.out.print(rc + " ");
                    System.out.println();
                }
            }
            checkoutManager.returnInstrument(instrument.getId(), condition);
            System.out.println("Вы успешно вернули прибор!");

        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

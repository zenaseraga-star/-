package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;

import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;


import java.util.Locale;
import java.util.Scanner;

public class CheckoutReturnCommand implements Command{
    private final CheckoutManager checkoutManager;
    private final Scanner scanner;

    public CheckoutReturnCommand(CheckoutManager checkoutManager, Scanner scanner) {
        this.checkoutManager = checkoutManager;
        this.scanner = scanner;
    }


    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 2){
                throw new ValidationException("нужно ввести id выдачи!");
            }
            long checkoutId = Long.parseLong(args[1]);

            System.out.println("Введите состояние прибора (OK или DAMAGED): ");
            String condition = this.scanner.nextLine().toLowerCase(Locale.ROOT);

            ReturnCondition finalConditionl = switch (condition) {
                case "ok" -> ReturnCondition.OK;
                case "damaged" -> ReturnCondition.DAMAGED;
                default -> {throw new ValidationException("Неизвестное состояние. Введите OK или DAMAGED");}
            };

            checkoutManager.returnInstrument(checkoutId, finalConditionl);
            System.out.println("Вы успешно вернули прибор!");

        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом!");
        }
    }
}

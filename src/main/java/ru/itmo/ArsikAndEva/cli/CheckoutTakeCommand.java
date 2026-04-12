package ru.itmo.ArsikAndEva.cli;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;

import java.time.Instant;
import java.util.Scanner;

public class CheckoutTakeCommand implements Command{
    private final InstrumentManager instrumentManager;
    private final CheckoutManager checkoutManager;
    private final Scanner scanner;

    public CheckoutTakeCommand(InstrumentManager instrumentManager, CheckoutManager checkoutManager, Scanner scanner) {
        this.instrumentManager = instrumentManager;
        this.checkoutManager = checkoutManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) {
        Instrument instrument = null;
        while (instrument == null){
            System.out.print("Введите id прибора: ");
            String input = scanner.nextLine();
            try {
                long instId = Long.parseLong(input);
                instrument = instrumentManager.getById(instId).orElse(null);

                if (instrument == null){
                    System.out.println("Инструмент с таким id не найден, попробуйте еще раз!");
                }

                if (instrument.getStatus() != InstrumentStatus.ACTIVE){
                    System.out.println("Этот прибор сейчас недоступен (cтатус: " + instrument.getStatus() + ")");
                    instrument = null;
                    continue;
                }

            } catch (NumberFormatException e) {
                System.out.println("id должен быть числом!");
            }

        }

        String username = "";
        while (username.isBlank()) {
            System.out.print("Кто берет: ");
            username = scanner.nextLine();

            if (username.isBlank())
                System.out.println("ФИО не может быть пустым!");
        }

        System.out.print("Комментарий (можно пусто): ");
        String comment = this.scanner.nextLine();

        Checkout checkout = new Checkout(0, instrument.getId(), username, comment, Instant.now(), null, null, "System", Instant.now());
        checkoutManager.add(checkout);

        System.out.println("OK checkout_id = " + checkout.getId());
    }
}

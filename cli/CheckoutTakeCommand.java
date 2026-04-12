package ru.itmo.ArsikAndEva.cli;
import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.model.Checkout;

import java.time.Instant;
import java.util.Scanner;

public class CheckoutTakeCommand implements Command{
    private final CheckoutManager checkoutManager;
    private final Scanner scanner;

    public CheckoutTakeCommand(CheckoutManager checkoutManager, Scanner scanner) {
        this.checkoutManager = checkoutManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 2) {
                throw new ValidationException("Введите id");
            }

            long instId = Long.parseLong(args[1]);

            System.out.println("Кто берет: ");
            String username = this.scanner.nextLine();
            System.out.println("Комментарий (можно пусто): ");
            String comment = this.scanner.nextLine();

            Checkout checkout = new Checkout(1, instId, username, comment, Instant.now(), null, null, "System", Instant.now());
            checkoutManager.add(checkout);

            System.out.println("OK checkout_id = " + checkout.getId());
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("id должен быть числом!");
        }
    }
}

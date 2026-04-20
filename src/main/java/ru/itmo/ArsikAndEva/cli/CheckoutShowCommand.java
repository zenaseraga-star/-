package main.java.ru.itmo.ArsikAndEva.cli;

import main.java.ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import main.java.ru.itmo.ArsikAndEva.exception.ValidationException;
import main.java.ru.itmo.ArsikAndEva.manager.CheckoutManager;
import main.java.ru.itmo.ArsikAndEva.model.Checkout;

public class CheckoutShowCommand implements Command{
    private final CheckoutManager checkoutManager;

    public CheckoutShowCommand(CheckoutManager checkoutManager) {
        this.checkoutManager = checkoutManager;
    }

    @Override
    public void execute(String[] args) {
        try {
            if (args.length < 2){
                throw new ValidationException("Обязательно нужно ввести номер выдачи!");
            }
            long checkoutId = Long.parseLong(args[1]);

            Checkout checkout = checkoutManager.getById(checkoutId).orElseThrow(() -> new EntityNotFoundException("Выдача с таким номером не найдена"));

            System.out.println(checkout);
        } catch (ValidationException | EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Номер выдачи нужно написать цифрами!");
        }
    }
}

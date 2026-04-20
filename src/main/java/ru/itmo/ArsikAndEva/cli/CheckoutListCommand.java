package main.java.ru.itmo.ArsikAndEva.cli;

import main.java.ru.itmo.ArsikAndEva.manager.CheckoutManager;
import main.java.ru.itmo.ArsikAndEva.model.Checkout;

import java.util.List;

public class CheckoutListCommand implements Command{
    private final CheckoutManager checkoutManager;

    public CheckoutListCommand(CheckoutManager checkoutManager) {
        this.checkoutManager = checkoutManager;
    }


    @Override
    public void execute(String[] args) {
        List<Checkout> checkoutList = checkoutManager.getAll();
        if (checkoutList.isEmpty()){
            System.out.println("Список выдач пока что пуст. Можно взять прибор, тогда нужно обязательно отметиться. Делается это с помощью команды checkout_take");
            return;
        }

        checkoutList.forEach(System.out::println);
    }
}
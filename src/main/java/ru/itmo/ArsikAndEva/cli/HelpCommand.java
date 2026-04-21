package main.java.ru.itmo.ArsikAndEva.cli;

import org.w3c.dom.ls.LSOutput;

public class HelpCommand implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println( "Команды, которые мы написали: ");
        System.out.println("  book_create <instrument_id>     - создать бронь");
        System.out.println("  book_list <instrument_id>   - список броней");
        System.out.println("  book_show <booking_id>          - показать бронь");
        System.out.println("  book_cancel <booking_id>        - отменить бронь");
        System.out.println("  book_reschedule <id>            - перенести бронь");
        System.out.println("  inst_add             -          добавить инструмент");
        System.out.println("  book_update <booking_id>        - обновить бронь");
        System.out.println("  checkout_take <instrument_id>   - взять прибор");
        System.out.println("  checkout_return <checkout_id>   - вернуть прибор");
        System.out.println("  checkout_list [--open-only]     - список выдач");
        System.out.println("  inst_available - доступные приборы");
        System.out.println("  help                            - список команд");
        System.out.println("  exit                            - выход");
    }
}
package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.exception.EntityNotFoundException;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.storage.AllData;
import ru.itmo.ArsikAndEva.storage.FileStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class SaveCommand implements Command{
    private final Scanner scanner;
    private final BookingManager bookingManager;
    private final InstrumentManager instrumentManager;
    private final CheckoutManager checkoutManager;
    private FileStorage storage;
    public SaveCommand(Scanner scanner, BookingManager bookingManager, InstrumentManager instrumentManager, CheckoutManager checkoutManager, FileStorage storage){
        this.bookingManager = bookingManager;
        this.scanner =scanner;
        this.checkoutManager =checkoutManager;
        this.instrumentManager =instrumentManager;
        this.storage = storage;
    }

    @Override
    public void execute(String[] args) {
        String filepath = null;

        if(args.length > 1 && args[1] != null && !args[1].trim().isEmpty()){
                filepath = args[1];
        }
        else {
            System.out.println("Введите путь к файлу:");
            while (filepath == null || filepath.isEmpty()) {

                String zn = scanner.nextLine().trim();
                if (zn.isEmpty()) {
                    System.out.println("Путь не может быть пустым. Введите снова:");
                } else {
                    filepath = zn;
                }
            }
        }
        storage = new FileStorage(filepath);
        AllData allData = new AllData(bookingManager.getData(), instrumentManager.getData(), checkoutManager.getData());
        try {
            storage.save(allData);
            System.out.println("Данные успешно сохранены!");
        }
            catch (FileNotFoundException e){
                System.out.println("Файл не найден, проверьте путь");
            }
         catch (IOException ex) {
        System.out.println(("Ошибка при сохранении"));
             System.out.println("Полный путь: " + new File(filepath).getAbsolutePath());
             ex.printStackTrace();
        }


    }
}

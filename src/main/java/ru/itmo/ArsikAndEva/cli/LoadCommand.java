package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.storage.AllData;
import ru.itmo.ArsikAndEva.storage.FileStorage;
import ru.itmo.ArsikAndEva.validator.FailValidator;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

public class LoadCommand implements Command {
    private final Scanner scanner;
    private final BookingManager bookingManager;
    private final CheckoutManager checkoutManager;
    private final InstrumentManager instrumentManager;
    public LoadCommand(Scanner scanner, BookingManager bookingManager, CheckoutManager checkoutManager, InstrumentManager instrumentManager) {
        this.bookingManager = bookingManager;
        this.checkoutManager = checkoutManager;
        this.scanner = scanner;
        this.instrumentManager = instrumentManager;
    }

    @Override
    public void execute(String[] args) {

        String filepath = null;

        if (args.length > 1  && !args[1].trim().isEmpty()) {
            filepath = args[1].trim();
        } else {
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

       FileStorage fileStorage = new FileStorage(filepath);
        try {
            AllData loadedAllData = fileStorage.load();
            FailValidator failValidator = new FailValidator();
            failValidator.validate(loadedAllData);
            bookingManager.loadData(loadedAllData.bookings());
            checkoutManager.loadData(loadedAllData.checkouts());
            instrumentManager.loadData(loadedAllData.instruments());

                System.out.println("Данные успешно загружены!");
        }
        catch (FileNotFoundException e){
            System.out.println("Файл не найден");
        }
        catch (InvalidObjectException | OptionalDataException | StreamCorruptedException e){
            System.out.println("Файл поврежден");
        }
        catch ( IOException | ClassNotFoundException ex) {
            System.out.println("Ошибка при загрузке");
            System.out.println("Полный путь: " + new File(filepath).getAbsolutePath());
            ex.printStackTrace();
        }

    }
}

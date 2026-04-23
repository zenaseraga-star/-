package ru.itmo.ArsikAndEva.cli;

import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.storage.AllData;
import ru.itmo.ArsikAndEva.storage.FileStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class LoadCommand implements Command {
    private final Scanner scanner;
    private final BookingManager bookingManager;
    private final CheckoutManager checkoutManager;
    private final InstrumentManager instrumentManager;
    private FileStorage fileStorage;

    public LoadCommand(Scanner scanner, BookingManager bookingManager, CheckoutManager checkoutManager, InstrumentManager instrumentManager, FileStorage fileStorage) {
        this.bookingManager = bookingManager;
        this.fileStorage = fileStorage;
        this.checkoutManager = checkoutManager;
        this.scanner = scanner;
        this.instrumentManager = instrumentManager;
    }

    @Override
    public void execute(String[] args) {

        String filepath = null;


        if (args.length > 1 && args[1] != null && !args[1].toUpperCase().trim().isEmpty()) {
            filepath = args[1];
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
        fileStorage = new FileStorage(filepath);
        try {
            AllData loadedAllData = fileStorage.load();
            bookingManager.loadData(loadedAllData.bookings());
            checkoutManager.loadData(loadedAllData.checkouts());
            instrumentManager.loadData(loadedAllData.instruments());
            System.out.println("Данные успешно загружены!");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, проверьте путь");
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println(("Ошибка при загрузке"));
            System.out.println("Полный путь: " + new File(filepath).getAbsolutePath());
            ex.printStackTrace();
        }
    }
}

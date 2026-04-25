package ru.itmo.ArsikAndEva.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.storage.AllData;
import ru.itmo.ArsikAndEva.storage.FileStorage;
import ru.itmo.ArsikAndEva.ui.tab.BookingTab;
import ru.itmo.ArsikAndEva.ui.tab.CheckoutTab;
import ru.itmo.ArsikAndEva.ui.tab.InstrumentTab;

import static ru.itmo.ArsikAndEva.ui.alert.AlertService.*;

public class MainGui extends Application {
    private InstrumentTab instrumentTabContent;
    private BookingTab bookingTabContent;
    private CheckoutTab checkoutTabContent;


    private final InstrumentManager instrumentManager = new InstrumentManager();
    private final BookingManager bookingManager = new BookingManager(instrumentManager);
    private final CheckoutManager checkoutManager = new CheckoutManager(instrumentManager);

    private final FileStorage fileStorage = new FileStorage("data.txt");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система управления лабораторией");

        loadDataOnStart();

        TabPane tabPane = new TabPane();

        Tab instrumentTab = createInstrumentTab();
        Tab bookingTab = createBookingTab();
        Tab checkoutTab = createCheckoutTab();

        tabPane.getTabs().addAll(instrumentTab, bookingTab, checkoutTab);

        BorderPane root = new BorderPane();
        root.setBottom(createControlPanel());
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createInstrumentTab() {
        InstrumentTab instrumentTabContent = new InstrumentTab(instrumentManager);

        this.instrumentTabContent = new InstrumentTab(instrumentManager); // инициализируем поле
        Tab tab = new Tab("Приборы", instrumentTabContent);
        tab.setClosable(false);
        return tab;
    }

    private Tab createBookingTab() {
        this.bookingTabContent = new BookingTab(bookingManager);
        Tab bookingTab = new Tab("Бронирования", bookingTabContent);
        bookingTab.setClosable(false);

        return bookingTab;
    }

    private Tab createCheckoutTab() {
        this.checkoutTabContent = new CheckoutTab(checkoutManager);
        Tab checkoutTab = new Tab("Выдачи");
        checkoutTab.setClosable(false);

        return checkoutTab;
    }

    private void handleSave() {
        try {
            AllData allData = new AllData(
                    bookingManager.getData(),
                    instrumentManager.getData(),
                    checkoutManager.getData()
            );

            fileStorage.save(allData);

            showInfo("Успех", "Данные успешно сохранены!");
        } catch (Exception e) {
            showError("Ошибка", "Не удалось сохранить: " + e.getMessage());
        }
    }

    private void handleLoad() {
        try {
            AllData loaded = fileStorage.load();

            instrumentManager.loadData(new java.util.HashMap<>(loaded.instruments()));

            showInfo("Успех", "Данные загружены! Нажмите \"Обновить\" на вкладках");
        } catch (Exception e) {
            showError("Ошибка", "Не удалось загрузить: " + e.getMessage());
        }
    }

    private HBox createControlPanel(){
        Button saveButton = new Button("Сохранить");
        Button loadButton = new Button("Загрузить");

        saveButton.setOnAction(e -> handleSave());
        loadButton.setOnAction(e -> handleLoad());

        HBox panel = new HBox(10, saveButton, loadButton);
        panel.setPadding(new Insets(10));

        return panel;
    }

    private void loadDataOnStart() {
        try {
            AllData loaded = fileStorage.load();

            instrumentManager.loadData(new java.util.HashMap<>(loaded.instruments()));

            System.out.println("Данные загружены из файла при старте.");
        } catch (Exception e) {
            System.out.println("Файл данных не загружен при старте: " + e.getMessage());
        }
    }

    private void refreshAll() {
        if (instrumentTabContent != null) {
            instrumentTabContent.refreshData();
        }
        if (bookingTabContent != null) {
            bookingTabContent.refreshData();
        }
        if (checkoutTabContent != null) {
            checkoutTabContent.refreshData();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
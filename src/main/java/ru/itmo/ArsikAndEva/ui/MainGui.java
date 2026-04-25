package ru.itmo.ArsikAndEva.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.storage.AllData;
import ru.itmo.ArsikAndEva.storage.FileStorage;
import ru.itmo.ArsikAndEva.ui.tab.InstrumentTab;

public class MainGui extends Application {
    private final InstrumentManager instrumentManager = new InstrumentManager();
    private final BookingManager bookingManager = new BookingManager(instrumentManager);
    private final CheckoutManager checkoutManager = new CheckoutManager(instrumentManager);

    private final FileStorage fileStorage = new FileStorage("data.txt");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система управления лабораторией");

        TabPane tabPane = new TabPane();

        Tab instrumentTab = createInstrumentTab();
        Tab bookingTab = createBookingTab();
        Tab checkoutTab = createCheckoutTab();

        tabPane.getTabs().addAll(instrumentTab, bookingTab, checkoutTab);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createInstrumentTab() {
        InstrumentTab instrumentTabContent = new InstrumentTab(instrumentManager);

        Tab instrumentTab = new Tab("Приборы");
        instrumentTab.setContent(instrumentTabContent);
        instrumentTab.setClosable(false);

        return instrumentTab;
    }

    private Tab createBookingTab() {
        Tab bookingTab = new Tab("Бронирования");
        bookingTab.setClosable(false);

        return bookingTab;
    }

    private Tab createCheckoutTab() {
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

            showAlert("Успех", "Данные успешно сохранены!", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось сохранить: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleLoad() {
        try {
            AllData loaded = fileStorage.load();

            instrumentManager.loadData(new java.util.HashMap<>(loaded.instruments()));

            showAlert("Успех", "Данные загружены! Нажмите Refresh на вкладках.", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Ошибка", "Не удалось загрузить: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
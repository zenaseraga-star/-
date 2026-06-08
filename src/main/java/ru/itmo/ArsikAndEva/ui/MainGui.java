package ru.itmo.ArsikAndEva.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.stage.Stage;
import ru.itmo.ArsikAndEva.db.BookingRepository;
import ru.itmo.ArsikAndEva.db.CheckoutRepository;
import ru.itmo.ArsikAndEva.db.InstrumentRepository;
import ru.itmo.ArsikAndEva.db.UserRepository;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
//import ru.itmo.ArsikAndEva.storage.AllData;
//import ru.itmo.ArsikAndEva.storage.FileStorage;
//import ru.itmo.ArsikAndEva.storage.UserStorage;
import ru.itmo.ArsikAndEva.ui.dialog.UserDialog;
import ru.itmo.ArsikAndEva.ui.tab.BookingTab;
import ru.itmo.ArsikAndEva.ui.tab.CheckoutTab;
import ru.itmo.ArsikAndEva.ui.tab.InstrumentTab;
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.users.UserManager;

public class MainGui extends Application {
    private InstrumentTab instrumentTabContent;
    private BookingTab bookingTabContent;
    private CheckoutTab checkoutTabContent;
    private  SessionManager sessionManager = new SessionManager();
    private final InstrumentManager instrumentManager = new InstrumentManager(new InstrumentRepository());
    private final BookingManager bookingManager = new BookingManager(instrumentManager, new BookingRepository());
    private final CheckoutManager checkoutManager = new CheckoutManager(instrumentManager, new CheckoutRepository());
    private final UserManager userManager = new UserManager(new UserRepository());
//    private final FileStorage fileStorage = new FileStorage("data.ser");


    @Override
    public void start(Stage primaryStage) {
        UserDialog authDialog = new UserDialog(userManager, sessionManager);
        authDialog.showAndWait();
        if (!sessionManager.isExistUser()) {
            System.out.println("Авторизация не выполнена");
            Platform.exit();
            return;
        }

        primaryStage.setTitle("Система управления лабораторией" + sessionManager.getCurrentUser().getLogin());

//        loadDataOnStart();

        TabPane tabPane = new TabPane();

        Tab instrumentTab = createInstrumentTab();
        Tab bookingTab = createBookingTab();
        Tab checkoutTab = createCheckoutTab();

        tabPane.getTabs().addAll(instrumentTab, bookingTab, checkoutTab);

        BorderPane root = new BorderPane();
//        root.setBottom(createControlPanel(primaryStage));
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createInstrumentTab() {
        this.instrumentTabContent = new InstrumentTab(instrumentManager, sessionManager, userManager);
        Tab tab = new Tab("Приборы", instrumentTabContent);
        tab.setClosable(false);
        return tab;
    }

    private Tab createBookingTab() {
        this.bookingTabContent = new BookingTab(bookingManager, instrumentManager, sessionManager, userManager);
        Tab bookingTab = new Tab("Бронирования", bookingTabContent);
        bookingTab.setClosable(false);
        return bookingTab;
    }

    private Tab createCheckoutTab() {
        this.checkoutTabContent = new CheckoutTab(checkoutManager, instrumentManager, sessionManager, userManager);
        Tab checkoutTab = new Tab("Выдачи", checkoutTabContent);
        checkoutTab.setClosable(false);
        return checkoutTab;
    }

//    private void handleSave(Stage primaryStage) {
//        try {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setInitialFileName("C:\\Users\\1\\IdeaProjects\\-\\data.ser");
//            fileChooser.setTitle("Сохранение данных");
//            fileChooser.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter("Serialized Files", "*.ser"),
//                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
//                    new FileChooser.ExtensionFilter("Data Files", "*.dat"),
//                    new FileChooser.ExtensionFilter("Bin Files", "*.bin"),
//                    new FileChooser.ExtensionFilter("All Files", "*.*"));
//            File selectedFile = fileChooser.showSaveDialog(primaryStage);
//            if (selectedFile==null){
//                return;
//            }
//            String filepath = selectedFile.getAbsolutePath();
//            if (!filepath.endsWith(".txt") && !filepath.endsWith(".bin")&& !filepath.endsWith(".ser") && !filepath.endsWith(".dat")){
//                filepath += ".ser";
//                selectedFile = new File(filepath);
//            }
////            FileStorage fileStorage1 = new FileStorage(selectedFile.getAbsolutePath());
////
////
////            AllData allData = new AllData(
////                    bookingManager.getData(),
////                    instrumentManager.getData(),
////                    checkoutManager.getData()
////            );
////            fileStorage1.save(allData);
//
//            showInfo("Успех", "Данные успешно сохранены!");
//        } catch (Exception e) {
//            showError("Ошибка", "Не удалось сохранить: " + e.getMessage());
//        }
//    }

//    private void handleLoad(Stage primaryStage) {
//        try {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Загрузка данных");
//            fileChooser.getExtensionFilters().addAll(
//                    new FileChooser.ExtensionFilter("Serialized Files", "*.ser"),
//                    new FileChooser.ExtensionFilter("Text Files", "*.txt"),
//                    new FileChooser.ExtensionFilter("Data Files", "*.dat"),
//                    new FileChooser.ExtensionFilter("Bin Files", "*.bin"),
//                    new FileChooser.ExtensionFilter("All Files", "*.*"));
//            File selectedFile = fileChooser.showOpenDialog(primaryStage);
//            if (selectedFile==null){
//                return;
//            }
//
//            FileStorage fileStorage1 = new FileStorage(selectedFile.getAbsolutePath());
//            AllData loaded = fileStorage1.load();
//
//            instrumentManager.loadData(new HashMap<>(loaded.instruments()));
//            bookingManager.loadData(new HashMap<>(loaded.bookings()));
//            checkoutManager.loadData(new HashMap<>(loaded.checkouts()));
//
//            refreshAll();
//            showInfo("Успех", "Данные загружены и обновлены!");
//
//        } catch (Exception e) {
//            showError("Ошибка", "Не удалось загрузить: " + e.getMessage());
//        }
//    }

//    private HBox createControlPanel(Stage primaryStage){
//        Button saveButton = new Button("Сохранить");
//        Button loadButton = new Button("Загрузить");
//
//        saveButton.setOnAction(e -> handleSave(primaryStage));
//        loadButton.setOnAction(e -> handleLoad(primaryStage));
//
//        HBox panel = new HBox(10, saveButton, loadButton);
//        panel.setPadding(new Insets(10));
//        return panel;
//    }

//    private void loadDataOnStart() {
//        try {
//            AllData loaded = fileStorage.load();
//            instrumentManager.loadData(new HashMap<>(loaded.instruments()));
//            bookingManager.loadData(new HashMap<>(loaded.bookings()));
//            checkoutManager.loadData(new HashMap<>(loaded.checkouts()));
//            System.out.println("Данные загружены из файла при старте.");
//        } catch (Exception e) {
//            System.out.println("Файл данных не загружен при старте: " + e.getMessage());
//        }
//    }
//
//    private void refreshAll() {
//        if (instrumentTabContent != null) instrumentTabContent.refreshData();
//        if (bookingTabContent != null) bookingTabContent.refreshData();
//        if (checkoutTabContent != null) checkoutTabContent.refreshData();
//    }

    public static void main(String[] args) {
        launch(args);
    }
}
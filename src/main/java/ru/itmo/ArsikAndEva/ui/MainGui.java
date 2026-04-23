package ru.itmo.ArsikAndEva.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainGui extends Application{
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Система управления лабораторией");

        TabPane tabPane = new TabPane();

        Tab instrumentTab = new Tab("Приборы");
        instrumentTab.setClosable(false);

        Tab bookingTab = new Tab("Бронирования");
        bookingTab.setClosable(false);

        Tab checkoutTab = new Tab("Выдачи");
        checkoutTab.setClosable(false);

        tabPane.getTabs().addAll(instrumentTab, bookingTab, checkoutTab);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
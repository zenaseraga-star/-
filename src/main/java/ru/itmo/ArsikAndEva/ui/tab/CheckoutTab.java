package ru.itmo.ArsikAndEva.ui.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.ui.dialog.CheckoutDialog;

import java.time.Instant;
import java.util.Optional;

public class CheckoutTab extends VBox {
    private final CheckoutManager checkoutManager;
    private final InstrumentManager instrumentManager;
    private final TableView<Checkout> table = new TableView<>();
    private final ObservableList<Checkout> data = FXCollections.observableArrayList();

    public CheckoutTab(CheckoutManager checkoutManager, InstrumentManager instrumentManager) {
        this.checkoutManager = checkoutManager;
        this.instrumentManager = instrumentManager;

        setSpacing(10);
        setPadding(new Insets(10));

        setupTable();

        getChildren().addAll(new Label("Список выдач:"), table, createButtons());

        refreshData();
    }

    private void setupTable() {
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Checkout, Long> idCol = new TableColumn<>("ID Выдачи");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Checkout, String> usernameCol = new TableColumn<>("ФИО взявшего");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Checkout, Long> instIdCol = new TableColumn<>("ID Прибора");
        instIdCol.setCellValueFactory(new PropertyValueFactory<>("instrumentId"));

        TableColumn<Checkout, Instant> takenCol = new TableColumn<>("Взят");
        takenCol.setCellValueFactory(new PropertyValueFactory<>("takenAt"));

        TableColumn<Checkout, Instant> returnedCol = new TableColumn<>("Вернут");
        returnedCol.setCellValueFactory(new PropertyValueFactory<>("returnedAt"));

        TableColumn<Checkout, String> instName = new TableColumn<>("Название прибора");
        instName.setCellValueFactory(e -> {
            long idIns = e.getValue().getId();
            String name = instrumentManager.getById(idIns).map(Instrument::getName).orElse("Не найден.");
            return new javafx.beans.property.SimpleStringProperty(name);
        });

        table.getColumns().addAll(idCol, usernameCol, instIdCol, instName, takenCol, returnedCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);
    }

    private HBox createButtons() {
        Button refreshButton = new Button("Обновить");
        Button addButton = new Button("Выдать");
        Button returnButton = new Button("Вернуть");
        Button deleteButton = new Button("Удалить");

        refreshButton.setOnAction(e -> refreshData());
        addButton.setOnAction(e -> addCheckout());
        returnButton.setOnAction(e -> handleReturn());
        deleteButton.setOnAction(e -> deleteSelectedCheckout());

        return new HBox(10, refreshButton, addButton, returnButton, deleteButton);
    }

    public void refreshData() {
        data.setAll(checkoutManager.getAll());
    }

    private void addCheckout() {
        Optional<Checkout> checkout = CheckoutDialog.showAddDialog(instrumentManager);

        checkout.ifPresent(chk -> {
            try {
                checkoutManager.add(chk);
                refreshData();
            } catch (Exception e) {
                AlertService.showError("Ошибка", "Не удалось оформить выдачу: " + e.getMessage());
            }
        });
    }

    private void deleteSelectedCheckout() {
        Checkout selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            checkoutManager.remove(selected.getId());
            refreshData();
            table.getSelectionModel().clearSelection();
            table.refresh();
        } else {
            AlertService.showWarning("Внимание", "Выберите запись для удаления");
        }
    }

    private void handleReturn() {
        Checkout selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertService.showWarning("Внимание", "Выберите запись для возврата");
            return;
        }

        if (selected.getReturnedAt() != null) {
            AlertService.showWarning("Ошибка", "Прибор уже был возвращен ранее");
            return;
        }

        ChoiceDialog<ReturnCondition> dialog = new ChoiceDialog<>(ReturnCondition.OK, ReturnCondition.values());
        dialog.setTitle("Возврат прибора");
        dialog.setHeaderText("В каком состоянии возвращен прибор?");
        dialog.setContentText("Состояние:");

        dialog.showAndWait().ifPresent(condition -> {
            try {
                checkoutManager.returnInstrument(selected.getId(), condition);
                refreshData();
                table.refresh();
            } catch (Exception e) {
                AlertService.showError("Ошибка", "Не удалось оформить возврат: " + e.getMessage());
            }
        });
    }
}
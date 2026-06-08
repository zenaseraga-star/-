package ru.itmo.ArsikAndEva.ui.tab;

import javafx.beans.property.SimpleStringProperty;
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
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.users.UserManager;

import java.time.Instant;
import java.util.Optional;

public class CheckoutTab extends VBox {
    private final CheckoutManager checkoutManager;
    private final InstrumentManager instrumentManager;
    private final TableView<Checkout> table = new TableView<>();
    private final ObservableList<Checkout> data = FXCollections.observableArrayList();
    private final SessionManager sessionManager;
    private final UserManager userManager;

    public CheckoutTab(CheckoutManager checkoutManager, InstrumentManager instrumentManager, SessionManager sessionManager, UserManager userManager) {
        this.checkoutManager = checkoutManager;
        this.instrumentManager = instrumentManager;
        this.sessionManager = sessionManager;
        this.userManager = userManager;

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
            long instrId = e.getValue().getInstrumentId();
            String name = instrumentManager.getById(instrId).map(Instrument::getName).orElse("Не найден.");
            return new SimpleStringProperty(name);
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

        returnButton.setDisable(true);
        deleteButton.setDisable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            boolean mine = isMine(sel);
            returnButton.setDisable(!mine);
            deleteButton.setDisable(!mine);
        });

        refreshButton.setOnAction(e -> refreshData());
        addButton.setOnAction(e -> addCheckout());
        returnButton.setOnAction(e -> handleReturn());
        deleteButton.setOnAction(e -> deleteSelectedCheckout());

        return new HBox(10, refreshButton, addButton, returnButton, deleteButton);
    }

    private boolean isMine(Checkout c) {
        if (c == null || c.getOwnerId() == null) return false;
        return c.getOwnerId().equals(sessionManager.getCurrentUser().getUsId());
    }

    public void refreshData()
    {
        data.setAll(checkoutManager.getAll());
    }

    private void addCheckout() {
        CheckoutDialog.showAddDialog(instrumentManager, sessionManager).ifPresent(chk -> {
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
        if (selected == null) {
            AlertService.showWarning("Внимание", "Выберите запись для удаления");
            return;
        }
        if (!isMine(selected)) {
            AlertService.showError("Ошибка", "У вас нет прав на удаление этой выдачи");
            return;
        }
        try {
            checkoutManager.remove(selected.getId());
            refreshData();
            table.getSelectionModel().clearSelection();
            table.refresh();
        } catch (Exception e) {
            AlertService.showError("Ошибка", e.getMessage());
        }
    }

    private void handleReturn() {
        Checkout selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertService.showWarning("Внимание", "Выберите запись для возврата");
            return;
        }

        if (!isMine(selected)) {
            AlertService.showError("Ошибка", "У вас нет прав на возврат этой выдачи");
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
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
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.ui.dialog.InstrumentDialog;
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.users.UserManager;

import java.util.Optional;


public class InstrumentTab extends VBox {
    private final InstrumentManager instrumentManager;
    private final TableView<Instrument> table = new TableView<>();
    private final ObservableList<Instrument> data = FXCollections.observableArrayList();
    private final SessionManager sessionManager;
    private final UserManager userManager;


    public InstrumentTab(InstrumentManager instrumentManager, SessionManager sessionManager, UserManager userManager) {
        this.instrumentManager = instrumentManager;
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        setSpacing(10);
        setPadding(new Insets(10));

        setupTable();

        getChildren().addAll(new Label("Список инструментов: "), table, createButtons());

        refreshData();
    }

    private void setupTable(){
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Instrument, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Instrument, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn<Instrument, InstrumentType> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));

        TableColumn<Instrument, InstrumentStatus> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory("status"));

        TableColumn<Instrument, String> ownerCol = new TableColumn<>("Owner");
        ownerCol.setCellValueFactory(cell -> new SimpleStringProperty(userManager.getLoginById(cell.getValue().getOwnerId())));

        table.getColumns().addAll(idCol, nameCol, typeCol, statusCol, ownerCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);
    }


    private HBox createButtons(){
        Button refreshButton = new Button("Обновить");
        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");

        deleteButton.setDisable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> deleteButton.setDisable(!isMine(newSel)));
        refreshButton.setOnAction(e -> refreshData());
        addButton.setOnAction(e -> addInstrument());
        deleteButton.setOnAction(e -> deleteSelectedInstrument());


        return new HBox(10, refreshButton, addButton, deleteButton);
    }

    private boolean isMine(Instrument inst) {
        if (inst == null || inst.getOwnerId() == null) return false;
        Long me = sessionManager.getCurrentUser().getUsId();
        return inst.getOwnerId().equals(me);
    }

    public void refreshData(){
        instrumentManager.loadAll();
        data.setAll(instrumentManager.getAll());
    }

    private void addInstrument(){
        Optional<Instrument> instrument = InstrumentDialog.showAddDialog(instrumentManager, sessionManager);

        instrument.ifPresent(inst -> refreshData());
    }

    private void deleteSelectedInstrument(){
        Instrument selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertService.showError("Ошибка", "Выберите прибор, который хотите удалить!");
            return;
        }
        if (!isMine(selected)) {
            AlertService.showError("Ошибка", "У вас нет прав на удаление этого прибора");
            return;
        }
        boolean del = AlertService.showConfirmation("Подтверждение удаления",
                "Вы точно хотите удалить прибор " + selected.getName() + " ?");
        if (!del) return;
        try {
            instrumentManager.remove(selected.getId());
            refreshData();
            table.getSelectionModel().clearSelection();
        } catch (RuntimeException ex) {
            AlertService.showError("Ошибка", ex.getMessage());
        }
    }
}

package ru.itmo.ArsikAndEva.ui.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.ui.dialog.InstrumentDialog;

import java.util.Optional;


public class InstrumentTab extends VBox {
    private final InstrumentManager instrumentManager;
    private final TableView<Instrument> table = new TableView<>();
    private final ObservableList<Instrument> data = FXCollections.observableArrayList();


    public InstrumentTab(InstrumentManager instrumentManager) {
        this.instrumentManager = instrumentManager;

        setSpacing(10);
        setPadding(new Insets(10));

        setupTable();

        getChildren().addAll(new Label("Список инструментов: "), table, createButtons());

        refreshData();
    }

    private void setupTable(){
        TableColumn<Instrument, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Instrument, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn<Instrument, InstrumentType> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));

        TableColumn<Instrument, InstrumentStatus> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory("status"));

        table.getColumns().addAll(idCol, nameCol, typeCol, statusCol);
        table.setItems(data);
    }


    private HBox createButtons(){
        Button refreshButton = new Button("Обновить");
        Button addButton = new Button("Добавить");
        Button deleteButton = new Button("Удалить");

        refreshButton.setOnAction(e -> refreshData());
        addButton.setOnAction(e -> addInstrument());
        deleteButton.setOnAction(e -> deleteSelectedInstrument());


        return new HBox(10, refreshButton, addButton, deleteButton);
    }

    public void refreshData(){
        data.setAll(instrumentManager.getAll());
    }

    private void addInstrument(){
        Optional<Instrument> instrument = InstrumentDialog.showAddDialog();

        instrument.ifPresent(inst -> {
            instrumentManager.add(inst);
            refreshData();
        });
    }

    private void deleteSelectedInstrument(){
        Optional<Instrument> selected = Optional.ofNullable(table.getSelectionModel().getSelectedItem());
        selected.ifPresentOrElse(
                e -> {

                    boolean del = AlertService.showConfirmation("Подтверждение удаления",
                            "Вы точно хотите удалить прибор " + e.getName() + " ?");

                    if (!del)
                        return;

                    instrumentManager.remove(e.getId());
                    refreshData();
                    table.getSelectionModel().clearSelection();
                },
                () -> AlertService.showError("Ошибка", "Выберите прибор, который хотите удалить!")
        );
    }
}

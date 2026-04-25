package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;

import java.util.Optional;

public class InstrumentDialog {

    private static GridPane createForm(TextField nameField, ComboBox<InstrumentType> typeBox,
                                       TextField inventoryNumberField, TextField locationField,
                                       ComboBox<InstrumentStatus> statusBox){
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.addRow(0, new Label("Название прибора:"), nameField);
        gridPane.addRow(1, new Label("Тип прибора:"), typeBox);
        gridPane.addRow(2, new Label("Инвентарный номер:"), inventoryNumberField);
        gridPane.addRow(3, new Label("Локация:"), locationField);
        gridPane.addRow(4, new Label("Статус:"), statusBox);

        return gridPane;
    }

    public static Optional<Instrument> showAddDialog() {
        Dialog<Instrument> instrumentDialog = new Dialog<>();
        instrumentDialog.setTitle("Окно добавления прибора");
        instrumentDialog.setHeaderText("Информация о приборе");

        ButtonType addButton = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        instrumentDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField inventoryNumberField = new TextField();
        TextField locationField = new TextField();

        ComboBox<InstrumentType> typeBox = new ComboBox<>();
        ComboBox<InstrumentStatus> statusBox = new ComboBox<>();

        typeBox.getItems().addAll(InstrumentType.values());

        statusBox.getItems().addAll(InstrumentStatus.values());

        GridPane form = createForm(
                nameField,
                typeBox,
                inventoryNumberField,
                locationField,
                statusBox
        );

        instrumentDialog.getDialogPane().setContent(form);

        instrumentDialog.setResultConverter(button -> {
            if (button != addButton){
                return null;
            }

            String name = nameField.getText().trim();
            String inventoryNumber = inventoryNumberField.getText().trim();
            String location = locationField.getText().trim();

            InstrumentType type = typeBox.getValue();
            InstrumentStatus status = statusBox.getValue();

            if (name.isBlank()) {
                AlertService.showError("Ошибка.", "Название прибора не может быть пустым!");
                return null;
            }

            if (inventoryNumber.isBlank()){
                AlertService.showError("Ошибка.", "Инвентарный номер должен быть заполнен!");
                return null;
            }

            if (location.isBlank()){
                AlertService.showError("Ошибка.", "Напишите где находится прибор!");
                return null;
            }

            if (type == null){
                AlertService.showError("Ошибка.", "Выберите тип прибора!");
                return null;
            }

            if (status == null){
                AlertService.showError("Ошибка.", "Выберите статус прибора!");
                return null;
            }

            return new Instrument(name, type, inventoryNumber, location, status);
        });

        return instrumentDialog.showAndWait();
    }
}

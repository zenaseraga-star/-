package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;

import java.util.Optional;

public class CheckoutDialog {

    private static GridPane createForm(TextField usernameField, ComboBox<Instrument> instBox, TextArea commentField) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.addRow(0, new Label("Кто берет:"), usernameField);
        gridPane.addRow(1, new Label("Прибор:"), instBox);
        gridPane.addRow(2, new Label("Комментарий:"), commentField);

        commentField.setPrefRowCount(3);
        return gridPane;
    }

    public static Optional<Checkout> showAddDialog(InstrumentManager instrumentManager) {
        Dialog<Checkout> checkoutDialog = new Dialog<>();
        checkoutDialog.setTitle("Окно добавления записи");
        checkoutDialog.setHeaderText("Информация о выдаче прибора");

        ButtonType saveButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        checkoutDialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField usernameField = new TextField();
        TextArea commentField = new TextArea();
        ComboBox<Instrument> instBox = new ComboBox<>();
        instBox.getItems().addAll(instrumentManager.getAll());

        checkoutDialog.getDialogPane().setContent(createForm(usernameField, instBox, commentField));

        Button actualButton = (Button) checkoutDialog.getDialogPane().lookupButton(saveButtonType);
        actualButton.addEventFilter(ActionEvent.ACTION, event -> {
            String username = usernameField.getText().trim();
            Instrument selected = instBox.getValue();

            if (username.isBlank() || selected == null) {
                AlertService.showError("Ошибка валидации", "Необходимо заполнить ФИО и выбрать инструмент!");
                event.consume();
            }
        });


        checkoutDialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Checkout(
                        instBox.getValue().getId(),
                        usernameField.getText().trim(),
                        commentField.getText()
                );
            }
            return null;
        });

        return checkoutDialog.showAndWait();
    }
}
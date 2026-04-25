package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;

import java.util.Optional;

public class CheckoutDialog {
    private final InstrumentManager instrumentManager;

    public CheckoutDialog(InstrumentManager instrumentManager) {
        this.instrumentManager = instrumentManager;
    }

    private static GridPane createForm(TextField username, ComboBox<Instrument> instIDs, TextArea comment){
        GridPane gridPane = new GridPane();

        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        gridPane.addRow(0, new Label("Кто берет: "), username);
        gridPane.addRow(1, new Label("ID инструмента"), instIDs);
        gridPane.addRow(2, new Label("Комментарий: "), comment);

        return gridPane;
    }

    public static Optional<Checkout> showAddDialog(InstrumentManager instrumentManager) {
        Dialog<Checkout> checkoutDialog = new Dialog<>();
        checkoutDialog.setTitle("Окно добавления записи");
        checkoutDialog.setHeaderText("Информация о записи");

        ButtonType addButton = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        checkoutDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        ComboBox<Instrument> instIDs = new ComboBox<>();
        instIDs.getItems().addAll(instrumentManager.getAll());

        TextField usernameField = new TextField();
        TextArea commentField = new TextArea();

        GridPane form = createForm(usernameField, instIDs, commentField);
        checkoutDialog.getDialogPane().setContent(form);

        checkoutDialog.setResultConverter(but -> {
            if (but != addButton){
                return null;
            }

            String username = usernameField.getText().trim();
            Long instID;

            if (instIDs.getValue() != null){
                instID = instIDs.getValue().getId();
            } else {
                AlertService.showError("Ошибка.", "Выберите ID прибора.");
                return null;
            }

            if (username.isBlank()){
                AlertService.showError("Ошибка.", "Нужно ввести кто берет!");
                return null;
            }

            return new Checkout(instID, username, commentField.getText());
        });

        return checkoutDialog.showAndWait();
    }
}

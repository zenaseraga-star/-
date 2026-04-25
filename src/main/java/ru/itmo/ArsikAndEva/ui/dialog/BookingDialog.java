package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.time.LocalDate;
import java.util.Optional;

public class BookingDialog {
    private static GridPane createForm(ComboBox<Instrument> insBox, TextField startAtField, TextField endAtField, DatePicker endDatePicker,
                                       DatePicker startDatePicker){
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.addRow(0, new Label("ID прибора"), insBox);
        gridPane.addRow(1, new Label("Начало(YYYY-MM-DD HH:MM):"), startAtField);
        gridPane.addRow(2, new Label("Конец(YYYY-MM-DD HH:MM):"), endAtField);
        gridPane.addRow(3, new Label("Дата начала:"), startDatePicker);
        gridPane.addRow(4, new Label("Дата конца:"), endDatePicker);
        return gridPane;
    }
    private static GridPane reForm(TextField startField,TextField endField){
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(1, new Label("Начало(YYYY-MM-DD HH:MM):"), startField);
        gridPane.addRow(2, new Label("Конец(YYYY-MM-DD HH:MM):"), endField);
        return gridPane;
    }
    public static boolean showRechDialog( Booking booking,BookingManager bookingManager){
        if(booking==null){
            AlertService.showError("Ошибка", "Бронирование не выбрано");
            return false;
        }
        Dialog<Booking> bookingDialog = new Dialog<>();
        bookingDialog.setTitle("Окно переноса брони");
        bookingDialog.setHeaderText("Перенос брони " + booking.getId());

        ButtonType addButton = new ButtonType("Перенос", ButtonBar.ButtonData.OK_DONE);
        bookingDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField startField = new TextField();
        startField.setPromptText("yyyy-mm-dd hh:mm");
        startField.setText(booking.getFormattedStart());

        TextField endField = new TextField();
        endField.setPromptText("yyyy-mm-dd hh:mm");
        endField.setText(booking.getFormattedEnd());

        GridPane form = reForm(
                startField,
                endField
        );

        bookingDialog.getDialogPane().setContent(form);
        bookingDialog.setResultConverter(button -> {
            if (button != addButton) {
                return null;
            }

            String start = startField.getText().trim();
            String end = endField.getText().trim();
            try {
                BookingValidator.validateTime(start);
                BookingValidator.validateTime(end);
                bookingManager.bookReschedule(booking.getId(), start, end);
                return booking;
            } catch (Exception e) {
                AlertService.showError("Ошибка", "Конец раньше начала");
                return null;
            }
        });
        return bookingDialog.showAndWait().isPresent();

    }
    public static Optional<Booking> showAddDialog(BookingManager bookingManager, InstrumentManager instrumentManager) {
        Dialog<Booking> bookingDialog = new Dialog<>();
        bookingDialog.setTitle("Окно создания брони");
        bookingDialog.setHeaderText("Информация о броне");

        ButtonType addButton = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        bookingDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        ComboBox<Instrument> instBox = new ComboBox<>();
        instBox.getItems().addAll(instrumentManager.getAll());

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Выберите дату начала");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Выберите дату конца");

        TextField startAtField = new TextField();
        TextField endAtField = new TextField();

        GridPane form = createForm(
                instBox,
                startAtField,
                endAtField,
                endDatePicker,
                startDatePicker

        );

        bookingDialog.getDialogPane().setContent(form);

        bookingDialog.setResultConverter(button -> {
            if (button != addButton){
               return null;
            }


            String start = startAtField.getText().trim();
            String end = endAtField.getText().trim();
            Optional<Instrument> instrument = Optional.ofNullable(instBox.getValue());
            if (instrument.isEmpty()) {
                AlertService.showError("Ошибка.", "Введите Id прибора");

            }
            Long id = instrument.get().getId();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            try{
                BookingValidator.validateTime(start);
                BookingValidator.validateTime(end);
            }
            catch (Exception e){
                AlertService.showError("Ошибка","Неверный формат даты!");
            }



            long bookId = bookingManager.createBook(id,start,end, "System");

            return bookingManager.getBookById(bookId);
        });

        return bookingDialog.showAndWait();
    }
}

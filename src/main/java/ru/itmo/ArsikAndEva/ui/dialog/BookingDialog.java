package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class BookingDialog {
    private static GridPane createForm(ComboBox<Instrument> insBox,  DatePicker endDatePicker,
                                       DatePicker startDatePicker, TextField startTimeField,  TextField endTimeField){
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.addRow(0, new Label("ID прибора"), insBox);
        gridPane.addRow(1, new Label("Время начала"), startTimeField);
        gridPane.addRow(2, new Label("Время конца"), endTimeField);
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
TextField startTimeField = new TextField();
TextField endTimeField = new TextField();
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Выберите дату начала");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Выберите дату конца");



        GridPane form = createForm(
                instBox,
                endDatePicker,
                startDatePicker,
                endTimeField,
                startTimeField





        );

        bookingDialog.getDialogPane().setContent(form);

        bookingDialog.setResultConverter(button -> {
            if (button != addButton){
               return null;
            }


            Optional<Instrument> instrument = Optional.ofNullable(instBox.getValue());
            if (instrument.isEmpty()) {
                AlertService.showError("Ошибка.", "Введите Id прибора");

            }
            Long id = instrument.get().getId();
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String startTimeStr = startTimeField.getText().trim();
            String endTimeStr = endTimeField.getText().trim();
            String startDateTimeStr = startDate.toString() + " " + startTimeStr;
            String endDateTimeStr = endDate.toString() + " " + endTimeStr;
            Instant start = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(startDateTimeStr));
            Instant end = Instant.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.systemDefault()).parse(endDateTimeStr));
            if (start.isAfter(end)) {
                throw new ValidationException(" Конец не может быть раньше начала ");
            }



            long bookId = bookingManager.createBook(id,startDateTimeStr ,endDateTimeStr, "System");

            return bookingManager.getBookById(bookId);
        });

        return bookingDialog.showAndWait();
    }
}

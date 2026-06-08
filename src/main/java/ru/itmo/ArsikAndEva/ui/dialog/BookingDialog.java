package ru.itmo.ArsikAndEva.ui.dialog;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ru.itmo.ArsikAndEva.exception.ValidationException;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.validator.BookingValidator;

import java.time.LocalDate;
import java.util.Optional;

public class BookingDialog {
    private static GridPane createForm(TextField ownerName, ComboBox<Instrument> insBox,  DatePicker endDatePicker,
                                       DatePicker startDatePicker, TextField startTimeField,  TextField endTimeField){
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(0, new Label("ФИО клиента"), ownerName);
        gridPane.addRow(1, new Label("ID прибора"), insBox);
        gridPane.addRow(2, new Label("Дата начала:"), startDatePicker);
        gridPane.addRow(3, new Label("Время начала"), startTimeField);
        gridPane.addRow(4, new Label("Дата конца:"), endDatePicker);
        gridPane.addRow(5, new Label("Время конца"), endTimeField);

        return gridPane;
    }
    private static GridPane reForm(DatePicker endDatePicker, DatePicker startDatePicker, TextField startTimeField,  TextField endTimeField){
        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.addRow(1, new Label("Дата начала:"), startDatePicker);
        gridPane.addRow(2, new Label("Время начала"), startTimeField);
        gridPane.addRow(3, new Label("Дата конца:"), endDatePicker);
        gridPane.addRow(4, new Label("Время конца"), endTimeField);
//        gridPane.addRow(1, new Label("Начало(YYYY-MM-DD HH:MM):"), startField);
//        gridPane.addRow(2, new Label("Конец(YYYY-MM-DD HH:MM):"), endField);
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

        TextField startTimeField = new TextField();
        TextField endTimeField = new TextField();

        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Выберите дату начала");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Выберите дату конца");

        GridPane form = reForm(
                endDatePicker,
                startDatePicker,
                startTimeField,
                endTimeField
        );

        bookingDialog.getDialogPane().setContent(form);

        Button actualButton = (Button) bookingDialog.getDialogPane().lookupButton(addButton);
        actualButton.addEventFilter(ActionEvent.ACTION, event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null | endDate == null){
                AlertService.showError("Ошибка", "Необходимо выбрать даты начала и конца");
                return;
            }

            String startDateTimeStr = startDate.toString() + " " + startTimeField.getText().trim();
            String endDateTimeStr = endDate.toString() + " " + endTimeField.getText().trim();

            try {
                BookingValidator.validateTime(startDateTimeStr);
                BookingValidator.validateTime(endDateTimeStr);
                bookingManager.bookReschedule(booking.getId(), startDateTimeStr, endDateTimeStr);
            } catch (Exception e){
                AlertService.showError("Ошибка", "Неверный формат даты");
                event.consume();
            }
        });

        bookingDialog.setResultConverter(button -> {
            if (button == addButton) {
                return booking;
            }
            return null;
        });
        return bookingDialog.showAndWait().isPresent();
    }
    public static Optional<Booking> showAddDialog(BookingManager bookingManager, InstrumentManager instrumentManager, SessionManager sessionManager) {
        Dialog<Booking> bookingDialog = new Dialog<>();
        bookingDialog.setTitle("Окно создания брони");
        bookingDialog.setHeaderText("Информация о броне");

        ButtonType addButton = new ButtonType("Создать", ButtonBar.ButtonData.OK_DONE);
        bookingDialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);
//        ComboBox<String> startTimeBox = new ComboBox<>();
//        startTimeBox.getItems().addAll("00", "01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23");
        TextField userName = new TextField();
        ComboBox<Instrument> instBox = new ComboBox<>();
        instBox.getItems().addAll(instrumentManager.getAll());
        TextField startTimeField = new TextField();
        TextField endTimeField = new TextField();
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Выберите дату начала");

        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Выберите дату конца");

        GridPane form = createForm(
                userName,
                instBox,
                endDatePicker,
                startDatePicker,
                startTimeField,
                endTimeField
        );

        bookingDialog.getDialogPane().setContent(form);

        long[] createdBookId = new long[1];

        Button actualButton = (Button) bookingDialog.getDialogPane().lookupButton(addButton);
        actualButton.addEventFilter(ActionEvent.ACTION, event -> {
                    Instrument instrument = instBox.getValue();

                    if (instrument == null) {
                        AlertService.showError("Ошибка", "Выберите прибор");
                        event.consume();
                        return;
                    }
                    String owner = userName.getText();
   if(owner == null){
       AlertService.showError("Ошибка", "Необходимо указать имя");
       event.consume();
       return;
   }
                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();

                    if (startDate == null | endDate == null) {
                        AlertService.showError("Ошибка", "Необходимо выбрать даты начала и конца");
                        event.consume();
                        return;
                    }

                    String startDateTimeStr = startDate.toString() + " " + startTimeField.getText().trim();
                    String endDateTimeStr = endDate.toString() + " " + endTimeField.getText().trim();

                    try {
                        BookingValidator.validateTime(startDateTimeStr);
                        BookingValidator.validateTime(endDateTimeStr);

                    } catch (Exception e) {
                        AlertService.showError("Ошибка", "Неверный формат даты");
                        event.consume();
                    }
                    try {
                        createdBookId[0] = bookingManager.createBook(instrument.getId(), startDateTimeStr, endDateTimeStr, sessionManager.getCurrentUser().getUsId(), owner);
                    }
        catch (ValidationException e){
                    AlertService.showError("Ошибка", e.getMessage());}
        });

            bookingDialog.setResultConverter(button -> {
                if (button == addButton) {
                    return bookingManager.getBookById(createdBookId[0]);
                }
                return null;
            });
            return bookingDialog.showAndWait();
        }
}
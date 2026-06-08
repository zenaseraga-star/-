package ru.itmo.ArsikAndEva.ui.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.enums.BookingStatus;
import ru.itmo.ArsikAndEva.ui.alert.AlertService;
import ru.itmo.ArsikAndEva.ui.dialog.BookingDialog;
import ru.itmo.ArsikAndEva.users.SessionManager;
import ru.itmo.ArsikAndEva.users.UserManager;

import java.util.Optional;

public class BookingTab extends VBox {
    private final BookingManager bookingManager;
    private final TableView<Booking> table = new TableView<>();
    private final ObservableList<Booking> data = FXCollections.observableArrayList();
    private final InstrumentManager instrumentManager;
    private final SessionManager sessionManager;
    private final UserManager userManager;

    public BookingTab(BookingManager bookingManager, InstrumentManager instrumentManager, SessionManager sessionManager, UserManager userManager) {
        this.bookingManager = bookingManager;
        this.instrumentManager = instrumentManager;
        this.sessionManager = sessionManager;
        this.userManager = userManager;

        setSpacing(10);
        setPadding(new Insets(10));
        setupTable();
        getChildren().addAll(new Label("Список брони"), table, createButtons());
        refreshData();
    }

    private void setupTable() {
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Booking, Long> bookIdCol = new TableColumn<>("ID брони");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Booking, Long> instId = new TableColumn<>("ID инструмента");
        instId.setCellValueFactory(new PropertyValueFactory<>("instrumentId"));

        TableColumn<Booking, String> startCol = new TableColumn<>("Начало");
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));
        startCol.setPrefWidth(100);

        TableColumn<Booking, String> endCol = new TableColumn<>("Конец");
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));
        endCol.setPrefWidth(100);

        TableColumn<Booking, BookingStatus> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Booking, String> ownCol = new TableColumn<>("Владелец");
        ownCol.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        TableColumn<Booking, String> crCol = new TableColumn<>("Создание");
        crCol.setCellValueFactory(new PropertyValueFactory<>("formattedCr"));
        crCol.setPrefWidth(100);

        table.getColumns().addAll(bookIdCol, instId, startCol, endCol, statusCol, ownCol, crCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(data);
    }

    public void refreshData() {
        data.setAll(bookingManager.getAll());
    }

    private boolean isMine(Booking b) {
        if (b == null || b.getOwnerId() == null) return false;
        return b.getOwnerId().equals(sessionManager.getCurrentUser().getUsId());
    }

    private HBox createButtons() {
        Button refreshButton = new Button("Обновить");
        Button addButton = new Button("Создать");
        Button deleteButton = new Button("Удалить");
        Button reButton = new Button("Перенести");

        refreshButton.setOnAction(e -> refreshData());
        addButton.setOnAction(e -> addBook());
        deleteButton.setDisable(true);
        reButton.setDisable(true);
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, sel) -> {
            boolean mine = isMine(sel);
            deleteButton.setDisable(!mine);
            reButton.setDisable(!mine);
        });
        deleteButton.setOnAction(e -> delSelectedBooking());
        reButton.setOnAction(e -> reSelectedBooking());

        return new HBox(10, refreshButton, addButton, deleteButton, reButton);
    }

    private void addBook() {
        Optional<Booking> booking = BookingDialog.showAddDialog(bookingManager, instrumentManager, sessionManager);
        refreshData();
    }

    private void delSelectedBooking() {
        Booking selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertService.showError("Ошибка", "Выберите бронь, которую хотите удалить");
            return;
        }
        if (!isMine(selected)) {
            AlertService.showError("Ошибка", "У вас нет прав на изменение этой брони");
            return;
        }
        boolean del = AlertService.showConfirmation("Подтверждение удаления", "Вы точно хотите удалить бронь?");
        if (!del) return;
        bookingManager.removeBooking(selected.getId());
        refreshData();
        table.getSelectionModel().clearSelection();
    }

    private void reSelectedBooking() {
        Booking selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertService.showError("Ошибка", "Выберите бронирование для переноса");
            return;
        }
        if (!isMine(selected)) {
            AlertService.showError("Ошибка", "У вас нет прав на изменение этой брони");
            return;
        }
        if (selected.getStatus() != BookingStatus.ACTIVE) {
            AlertService.showError("Ошибка", "Можно переносить только активные бронирования");
            return;
        }
        BookingDialog.showRechDialog(selected, bookingManager);
        refreshData();
    }
}
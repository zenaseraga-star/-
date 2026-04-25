
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
import javafx.scene.layout.VBox;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.manager.InstrumentManager;
import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.enums.BookingStatus;

public class BookingTab extends VBox {
    private final BookingManager bookingManager;
    private final TableView<Booking> table = new TableView<>();
    private final ObservableList<Booking> data = FXCollections.observableArrayList();
    public BookingTab(BookingManager bookingManager){
        this.bookingManager = bookingManager;
        setSpacing(10);
        setPadding(new Insets(10));
        setupTable();
        getChildren().addAll(new Label("Список брони"), table) ;
        refreshData();
    }
    private  void setupTable(){
        TableColumn<Booking, Long > bookIdCol = new TableColumn<>("ID брони");
        bookIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Booking, Long> instId = new TableColumn<>("ID инструмента");
        instId.setCellValueFactory(new PropertyValueFactory<>("instrumentId"));

        TableColumn<Booking, String> startCol = new TableColumn<>("Начало");
        startCol.setCellValueFactory(new PropertyValueFactory<>("formattedStart"));

        TableColumn<Booking, String> endCol = new TableColumn<>("Конец");
        endCol.setCellValueFactory(new PropertyValueFactory<>("formattedEnd"));

        TableColumn<Booking, BookingStatus> statusCol = new TableColumn<>("Статус");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Booking, String> ownCol = new TableColumn<>("Владелец");
        ownCol.setCellValueFactory(new PropertyValueFactory<>("ownerUsername"));

        TableColumn<Booking, String> crCol = new TableColumn<>("Создание");
        crCol.setCellValueFactory(new PropertyValueFactory<>("formattedCr"));

        table.getColumns().addAll(bookIdCol, instId, startCol, endCol, statusCol, ownCol, crCol);
        table.setItems(data);
    }
    private void refreshData(){
        data.setAll(bookingManager.getAll());
    }
    private HBox createButtons(){
        Button refreshButton = new Button("Обновить");
        Button addButton = new Button("Создать");
        Button deleteButton = new Button("Закрыть");

        refreshButton.setOnAction(e -> refreshData());
        addButton.setOnAction(e -> addBook());
        deleteButton.setOnAction(e -> cancelSelectedBooking());


        return new HBox(10, refreshButton, addButton, deleteButton);
    }
    private void addBook(){

    }
    private void cancelSelectedBooking(){

    }

}
package ru.itmo.ArsikAndEva.ui.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import ru.itmo.ArsikAndEva.manager.BookingManager;
import ru.itmo.ArsikAndEva.model.Booking;

public class BookingTab extends VBox {
    private final BookingManager bookingManager;
    private final TableView<Booking> table = new TableView<>();
    private final ObservableList<Booking> data = FXCollections.observableArrayList();


    public BookingTab(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }

      public void refreshData(){
        data.setAll(bookingManager.getAll());
    }
}

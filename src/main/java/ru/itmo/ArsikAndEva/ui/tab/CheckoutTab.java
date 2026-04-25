package ru.itmo.ArsikAndEva.ui.tab;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import ru.itmo.ArsikAndEva.manager.CheckoutManager;
import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.Instrument;
public class CheckoutTab extends VBox {
    private final CheckoutManager checkoutManager;
    private final TableView<Checkout> table = new TableView<>();
    private final ObservableList<Checkout> data = FXCollections.observableArrayList();

    public CheckoutTab(CheckoutManager checkoutManager) {
        this.checkoutManager = checkoutManager;
    }

    public void refreshData(){
        data.setAll(checkoutManager.getAll());
    }
}

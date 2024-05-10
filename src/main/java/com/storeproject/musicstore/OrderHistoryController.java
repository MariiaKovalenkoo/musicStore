package com.storeproject.musicstore;

import com.storeproject.musicstore.data.Database;
import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.model.Customer;
import com.storeproject.musicstore.model.Order;
import com.storeproject.musicstore.model.OrderedProduct;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class OrderHistoryController implements IController {
    @FXML
    private Label errorLabel;
    @FXML
    private TableView<Order> orderHistoryView;
    @FXML
    private TableColumn<Order, LocalDateTime> dateTimeColumn;
    @FXML
    private TableColumn<Order, String> customerNameColumn;
    @FXML
    private TableColumn<Order, Double> totalPriceColumn;
    @FXML
    private TableView<OrderedProduct> orderedProductsView;
    @FXML
    private TableColumn<OrderedProduct, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderedProduct, String> productNameColumn;
    @FXML
    private TableColumn<OrderedProduct, String> categoryColumn;
    @FXML
    private TableColumn<OrderedProduct, Double> priceColumn;

    private ObservableList<Order> orders;

    private ObservableList<OrderedProduct> orderedProducts;

    private Database database;

    @Override
    public void setDatabase(Database database){
        this.database = database;
        Map<String, Order> orderHistoryMap = database.getOrderHistory();
        orders = FXCollections.observableArrayList(orderHistoryMap.values());
    }

    @Override
    public void displayView() {
        try{
        initializeTableView();
        setupOrderSelectionListener();}
        catch (Exception e){
            showError("failed to load the history");
        }
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    private void initializeTableView() {
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));
        customerNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCustomer().getFullName()));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy ");
        dateTimeColumn.setCellFactory(column -> {
            return new TableCell<Order, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.format(formatter));
                    }
                }
            };
        });
        orderHistoryView.setItems(orders);
    }

    private void setupOrderSelectionListener() {
        orderHistoryView.getSelectionModel().selectedItemProperty().addListener((observable, oldOrder, newOrder) -> {
             if (newOrder != null) {
                orderedProducts = FXCollections.observableArrayList(newOrder.getOrderedProducts());
                initializeOrderedProductsTableView();
             } else {
               orderedProductsView.setItems(FXCollections.emptyObservableList());
             }
        });
    }

    private void initializeOrderedProductsTableView() {
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getName()));
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getProduct().getCategory()));
        priceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderedProduct, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<OrderedProduct, Double> data) {
                return new SimpleDoubleProperty(data.getValue().getProduct().getPrice()).asObject();
            }
        });
        orderedProductsView.setItems(orderedProducts);
    }
}
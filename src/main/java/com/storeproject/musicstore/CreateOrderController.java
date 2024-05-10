package com.storeproject.musicstore;

import com.storeproject.musicstore.data.Database;
import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.interfaces.IOrderManagement;
import com.storeproject.musicstore.model.Customer;
import com.storeproject.musicstore.model.Order;
import com.storeproject.musicstore.model.OrderedProduct;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CreateOrderController implements IController, IOrderManagement {
    @FXML
    private Label errorLabel;
    @FXML
    private TableView<OrderedProduct> orderedProductsTable;
    @FXML
    private Button addProductButton;
    @FXML
    private Button deleteOrderedProductButton;
    @FXML
    private Button createOrderButton;
    @FXML
    private TableColumn<OrderedProduct, Integer> quantityColumn;
    @FXML
    private TableColumn<OrderedProduct, String> nameColumn;
    @FXML
    private TableColumn<OrderedProduct, String> categoryColumn;
    @FXML
    private TableColumn<OrderedProduct, Double> priceColumn;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailAddressField;
    @FXML
    private TextField phoneNumberField;

    private Database database;

    private ObservableList<OrderedProduct> orderedProducts;

    private AddProductPopupController addProductPopupController;

    public void setAddProductPopupController(AddProductPopupController addProductPopupController){
        this.addProductPopupController = addProductPopupController;
    }

    @Override
    public void setDatabase(Database database){
        this.database = database;
    }

    @Override
    public void displayView() {
        orderedProducts = FXCollections.observableArrayList();
        initializeTableView();
        createOrderButton.setDisable(true);

        createOrderButton.disableProperty().bind(isCreateOrderEnabledBinding().not());

        deleteOrderedProductButton.setDisable(true);
        orderedProductsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteOrderedProductButton.setDisable(false);
            } else {
                deleteOrderedProductButton.setDisable(true);
            }
        });
    }

    private BooleanBinding isCreateOrderEnabledBinding() {
        BooleanBinding isOrderedProductsNotEmpty = Bindings.isNotEmpty(orderedProducts);
        BooleanBinding areTextFieldsFilled = firstNameField.textProperty().isNotEmpty()
                .and(lastNameField.textProperty().isNotEmpty())
                .and(emailAddressField.textProperty().isNotEmpty())
                .and(phoneNumberField.textProperty().isNotEmpty());

        return isOrderedProductsNotEmpty.and(areTextFieldsFilled);
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    private void initializeTableView() {
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProduct().getName()));
        categoryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getProduct().getCategory()));
        priceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderedProduct, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<OrderedProduct, Double> data) {
                return new SimpleDoubleProperty(data.getValue().getProduct().getPrice()).asObject();
            }
        });
        orderedProductsTable.setItems(orderedProducts);
    }

    // adding order product
    @FXML
    private void addProductOnClick() {
        openProductAddPopup();
    }

    private void openProductAddPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("product-add-popup.fxml"));
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add Product");

            Scene scene = new Scene(fxmlLoader.load());
            popupStage.setScene(scene);

            AddProductPopupController addProductController = fxmlLoader.getController();
            addProductController.displayView(database.getProductsInStock());
            addProductController.setParentController(this);
            setAddProductPopupController(addProductController);

            popupStage.showAndWait();
        } catch (IOException e) {
            showError("failed to open a popup");
        }
    }

    @Override
    public void addProductToOrder(OrderedProduct orderedProduct) {
        orderedProducts.add(orderedProduct);
    }

    // deleting product from the order
    @FXML
    private void deleteProductOnClick() {
       deleteProductFromOrder();
       orderedProductsTable.getSelectionModel().clearSelection();
    }

    @Override
    public void deleteProductFromOrder(){
        OrderedProduct selectedProduct = orderedProductsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct != null)
            orderedProducts.remove(selectedProduct);

        addProductPopupController.updateStock(selectedProduct.getProduct(), selectedProduct.getProduct().getStock() + selectedProduct.getQuantity());
    }

    // creating order
    @FXML
    private void createOrderOnClick() {
        if(orderedProducts.isEmpty())
            showError("Please add some products to the order");
        else
            openConfirmOrderPopup();
    }

    private void openConfirmOrderPopup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("confirm-order-popup.fxml"));
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Confirm Order");

            Scene scene = new Scene(fxmlLoader.load());
            popupStage.setScene(scene);

            ConfirmOrderPopUpController confirmOrderController = fxmlLoader.getController();
            confirmOrderController.setParentController(this);

            popupStage.showAndWait();
        } catch (IOException e) {
            showError("error while loading the popup");
        }
    }

    @Override
    public void createOrder()  {
        Order order = new Order();
        order.setCustomer(getCustomerInfo());

        order.setOrderDateTime(LocalDateTime.now());
        order.setOrderedProducts(new ArrayList<>(orderedProducts));
        try {
            database.createOrder(order);
        } catch (Exception e){
            showError("failed to create an order");
        }

        double totalPrice = 0;
        for(OrderedProduct orderedProduct : orderedProducts)
            totalPrice += orderedProduct.getProduct().getPrice();
        order.setTotalPrice(totalPrice);

        clearInput();
    }

    private Customer getCustomerInfo(){
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String emailAddress = emailAddressField.getText();
        String phoneNumber = phoneNumberField.getText();
        return new Customer(firstName, lastName, emailAddress, phoneNumber);
    }

    private void clearInput(){
        orderedProducts.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailAddressField.clear();
        phoneNumberField.clear();
    }
}
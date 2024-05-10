package com.storeproject.musicstore;


import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.interfaces.IPopupController;
import com.storeproject.musicstore.model.OrderedProduct;
import com.storeproject.musicstore.model.Product;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;


public class AddProductPopupController implements IPopupController {

    @FXML
    private TextField productSearchField;
    @FXML
    private Label errorAddlabel;
    @FXML
    private TableColumn<Product, Integer> stockColumn;
    @FXML
    private TableColumn<Product,String> nameColumn;
    @FXML
    private TableColumn<Product,String> categoryColumn;
    @FXML
    private TableColumn<Product,Double> priceColumn;
    @FXML
    private TableColumn<Product,String> descriptionColumn;
    @FXML
    private TableView<Product> productInventoryView;
    @FXML
    private TextField productQuantityField;
    @FXML
    private Button addToOrderButton;
    @FXML
    private Button cancelButton;

    private ObservableList<Product> productsInventory;

    private ObservableList<Product> filteredProducts;

    private CreateOrderController createOrderController;

    public void displayView(List<Product> productsInventory){
        this.productsInventory = FXCollections.observableArrayList(productsInventory);
        filteredProducts = FXCollections.observableArrayList(productsInventory);

        initializeTableView();

        BooleanBinding isQuantityFieldEmpty = productQuantityField.textProperty().isEmpty();
        addToOrderButton.disableProperty().bind(isQuantityFieldEmpty);

        productSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });
    }

    private void filterProducts(String searchText) {
        filteredProducts.clear();
        for (Product product : productsInventory) {
            if (searchText.length() < 3 || product.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
    }

    private void initializeTableView() {
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        //productInventoryView.setItems(productsInventory);
        productInventoryView.setItems(filteredProducts);
    }

    @Override
    public void setParentController(IController parentController) {
        this.createOrderController = (CreateOrderController) parentController;
    }

    public void updateStock(Product product, int newStock) {
        product.setStock(newStock);
    }

    @Override
    public void confirmAction() {
        Product selectedProduct = productInventoryView.getSelectionModel().getSelectedItem();
        String quantityText = productQuantityField.getText();

        if (selectedProduct != null) {
            try {
                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0 || quantity > selectedProduct.getStock()) {
                    showError("Please enter a valid quantity.");
                } else {
                    OrderedProduct orderedProduct = new OrderedProduct(selectedProduct, quantity);
                    createOrderController.addProductToOrder(orderedProduct);
                    updateStock(selectedProduct, selectedProduct.getStock() - quantity);

                    Stage stage = (Stage) addToOrderButton.getScene().getWindow();
                    stage.close();
                }
            } catch (NumberFormatException e) {
                showError("Please enter a valid quantity (a number).");
            }
        }
    }

    @Override
    public void cancelAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void showError(String errorMessage) {
        errorAddlabel.setText(errorMessage);
    }

    @FXML
    private void addToOrder() {
        confirmAction();
    }

    @FXML
    private void cancelAdding() {
        cancelAction();
    }
}
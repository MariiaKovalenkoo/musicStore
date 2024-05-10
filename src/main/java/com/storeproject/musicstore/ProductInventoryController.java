package com.storeproject.musicstore;

import com.storeproject.musicstore.data.CSVParser;
import com.storeproject.musicstore.data.Database;
import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.interfaces.IProductManagement;
import com.storeproject.musicstore.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductInventoryController implements IProductManagement, IController {
    @FXML
    private Button importProductsButton;
    @FXML
    private Label errorLabel;
    @FXML
    private Button buttonAddProduct;
    @FXML
    private Button buttonEditProduct;
    @FXML
    private Button buttonDeleteProduct;
    @FXML
    private TableView<Product> productInventoryView;
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

    private Database database;

    ObservableList<Product> productsInventory;

    @Override
    public void displayView() {
        productsInventory = FXCollections.observableArrayList(this.database.getProductsInventory());
        initializeTableView();

        buttonEditProduct.setDisable(true);
        buttonDeleteProduct.setDisable(true);

        productInventoryView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Enable the buttons when a product is selected
                buttonEditProduct.setDisable(false);
                buttonDeleteProduct.setDisable(false);
            } else {
                // Disable the buttons when no product is selected
                buttonEditProduct.setDisable(true);
                buttonDeleteProduct.setDisable(true);
            }
        });
    }

    @Override
    public void setDatabase(Database database){
        this.database = database;
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    private void updateInventoryView(){
        productsInventory.clear();
        productsInventory = FXCollections.observableArrayList(this.database.getProductsInventory());
        initializeTableView();
    }

    private void initializeTableView() {
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        productInventoryView.setItems(productsInventory);
    }

    @FXML
    private void addProductOnClick() {
        openNewProductPopup();
    }

    @FXML
    private void editProductOnClick() {
        Product selectedProduct = productInventoryView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            openEditProductPopup(selectedProduct);
        }
       updateInventoryView();
    }

    @FXML
    private void deleteProductOnClick() {
        deleteProduct();
    }

    @Override
    public void editProduct(Product product) {
        try{
        database.editProduct(product);
        updateInventoryView();}
        catch (Exception e){
            showError("failed to edit a product");
        }
    }

    @Override
    public void addNewProduct(Product product) {
        database.addNewProduct(product);
        updateInventoryView();;
    }

    @Override
    public void deleteProduct() {
        Product selectedProduct = productInventoryView.getSelectionModel().getSelectedItem();
        if(selectedProduct != null){
            database.deleteProduct(selectedProduct);
            updateInventoryView();
        }
    }

    private void openNewProductPopup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new-product-popup.fxml"));
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Add new Product");

            Scene scene = new Scene(fxmlLoader.load());
            popupStage.setScene(scene);

            NewProductPopupController newProductController = fxmlLoader.getController();
            newProductController.setParentController(this);

            popupStage.showAndWait();
        } catch (IOException e) {
            showError("failed to open a popup");
        }
    }

    private void openEditProductPopup(Product selectedProduct) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-product-popup.fxml"));
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Edit Product");

            Scene scene = new Scene(fxmlLoader.load());
            popupStage.setScene(scene);

            EditProductPopupController editProductController = fxmlLoader.getController();
            editProductController.setParentController(this);
            editProductController.setEditedProduct(selectedProduct);

            popupStage.showAndWait();
        } catch (IOException e) {
            showError("failed to open a popup");
        }
    }
    @FXML
    private void importProductsOnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(importProductsButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                CSVParser csvParser = new CSVParser();
                List<Product> importedProducts = csvParser.parseCSV(selectedFile);
                for (Product product : importedProducts) {
                    database.addNewProduct(product);
                }
                updateInventoryView();
            } catch (IOException e) {
                showError("An error occurred while importing products from the file");
            }
        }
    }
}
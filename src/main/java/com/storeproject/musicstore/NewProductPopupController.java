package com.storeproject.musicstore;

import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.interfaces.IPopupController;
import com.storeproject.musicstore.model.Product;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewProductPopupController implements IPopupController {
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField stockField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;

    private ProductInventoryController productInventoryController;

    @Override
    public void setParentController(IController parentController) {
        this.productInventoryController = (ProductInventoryController) parentController;
        confirmButton.disableProperty().bind(isFieldsValid().not());
    }

    private BooleanBinding isFieldsValid() {
        return Bindings.createBooleanBinding(() ->
                        !nameField.getText().isEmpty() &&
                                !categoryField.getText().isEmpty() &&
                                !descriptionField.getText().isEmpty() &&
                                !priceField.getText().isEmpty() &&
                                !stockField.getText().isEmpty(),
                nameField.textProperty(),
                categoryField.textProperty(),
                descriptionField.textProperty(),
                priceField.textProperty(),
                stockField.textProperty()
        );
    }

    @Override
    public void confirmAction() {
        String name = nameField.getText();
        String category = categoryField.getText();
        String priceText = priceField.getText();
        String stockText = stockField.getText();
        String description = descriptionField.getText();

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);
            Product newProduct = new Product(name, category, price, description, stock);
            productInventoryController.addNewProduct(newProduct);

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number");
        }
    }

    @Override
    public void cancelAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    @FXML
    private void confirmAddingNewProduct() {
        confirmAction();
    }

    @FXML
    private void cancelAdding() {
        cancelAction();
    }
}
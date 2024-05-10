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

public class EditProductPopupController implements IPopupController {
    @FXML
    private Label errorLabel;
    @FXML
    private Button cancelButton;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField stockField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;

    private ProductInventoryController productInventoryController;

    private Product editedProduct;

    @Override
    public void setParentController(IController parentController) {
        this.productInventoryController = (ProductInventoryController) parentController;
    }

    public void setEditedProduct(Product editedProduct){
        this.editedProduct = editedProduct;
        nameField.setText(editedProduct.getName());
        categoryField.setText(editedProduct.getCategory());
        descriptionField.setText(editedProduct.getDescription());
        priceField.setText(Double.toString(editedProduct.getPrice()));
        stockField.setText(Integer.toString(editedProduct.getStock()));

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

    @FXML
    private void confirmEditing() {
        confirmAction();
    }

    @FXML
    private void cancelEditing() {
        cancelAction();
    }

    @Override
    public void confirmAction() {
        String priceText = priceField.getText();
        String stockText= stockField.getText();

        try {
            double price = Double.parseDouble(priceText);
            int stock  = Integer.parseInt(stockText);

            editedProduct.setName(nameField.getText());
            editedProduct.setCategory(categoryField.getText());
            editedProduct.setPrice(price);
            editedProduct.setStock(stock);
            editedProduct.setDescription(descriptionField.getText());

            productInventoryController.editProduct(editedProduct);

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number");
        } catch (Exception e) {
            throw new RuntimeException(e);
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
}
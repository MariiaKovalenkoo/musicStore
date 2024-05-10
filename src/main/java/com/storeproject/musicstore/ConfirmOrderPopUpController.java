package com.storeproject.musicstore;

import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.interfaces.IPopupController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ConfirmOrderPopUpController implements IPopupController {
    @FXML
    private Label errorLabel;

    @FXML
    private Button cancelOrderButton;

    private CreateOrderController createOrderController;

    @Override
    public void setParentController(IController parentController) {
        this.createOrderController = (CreateOrderController) parentController;
    }

    @Override
    public void confirmAction() {
        createOrderController.createOrder();
        Stage stage = (Stage) cancelOrderButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void cancelAction() {
        Stage stage = (Stage) cancelOrderButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }

    @FXML
    private void confirmOrder() {
        try{
            confirmAction();
        }
        catch (Exception e){
            showError("an error occurred while creating a product");
        }
    }

    @FXML
    private void cancelOrderOnClick() {
        cancelAction();
    }
}
package com.storeproject.musicstore;

import com.storeproject.musicstore.data.Database;
import com.storeproject.musicstore.exceptions.LoadViewFailedException;
import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController implements IController {
    @FXML
    private Label errorMainView;

    @FXML
    private BorderPane mainViewPane;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button createOrderButton;

    @FXML
    private Button productInventoryButton;

    @FXML
    private Button orderHistoryButton;

    private Database database;

    private User loggedInUser;

    @Override
    public void setDatabase(Database database){
        this.database = database;
    }

    @Override
    public void displayView() {
       showDashboard();
    }

    @Override
    public void showError(String errorMessage) {
        errorMainView.setText(errorMessage);
    }

    public void setLoggedInUser(User loggedInUser){
        this.loggedInUser = loggedInUser;
        if(loggedInUser.getRole().equals(User.Role.SALES))
            productInventoryButton.setDisable(true);
        if(loggedInUser.getRole().equals(User.Role.MANAGER))
            createOrderButton.setDisable(true);
    }

    @FXML
    private void showDashboard() {
        showError("");
        try {
            DashboardController dashboardController = loadAndSetView("dashboard-view.fxml");
            dashboardController.setDatabase(database);
            dashboardController.setUser(loggedInUser);
            dashboardController.displayView();
        } catch (LoadViewFailedException e) {
            showError("failed to load the dashboard");
        }
    }

    @FXML
    private void showCreateOrder() {
        showError("");
        try{
        CreateOrderController createOrderController = loadAndSetView("create-order-view.fxml");
        createOrderController.setDatabase(database);
        createOrderController.displayView();
        } catch(LoadViewFailedException e){
            showError("failed to load the create order view");
        }
    }

    @FXML
    private void showProductInventory() {
       showError("");
        try{
        ProductInventoryController productInventoryControllerController = loadAndSetView("inventory-view.fxml");

        productInventoryControllerController.setDatabase(database);
        productInventoryControllerController.displayView();
        } catch(LoadViewFailedException e){
            showError("failed to load the product inventory");
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void showOrderHistory() {
        showError("");
        try{
        OrderHistoryController orderHistoryController = loadAndSetView("order-history-view.fxml");
        orderHistoryController.setDatabase(database);
        orderHistoryController.displayView();
        } catch(LoadViewFailedException e){
            showError("failed to load the order history");
        }
    }

    private <T> T loadAndSetView(String fxmlFileName) throws LoadViewFailedException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
            mainViewPane.setCenter(fxmlLoader.load());
            return fxmlLoader.getController();
        } catch (IOException e) {
            throw new LoadViewFailedException(e.getMessage());
        }
    }
}
package com.storeproject.musicstore;

import com.storeproject.musicstore.data.Database;
import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardController implements IController {
    @FXML
    private Label errorLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label dateTimeLabel;

    private User loggedInUser;

    private Database database;

    private void displayDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime currentDateTime = LocalDateTime.now();
        dateTimeLabel.setText(currentDateTime.format(formatter));
    }

    private void displayUserInfo(){
        welcomeLabel.setText(String.format("Welcome, %s!", this.loggedInUser.getFullName()));
        roleLabel.setText(String.format("Your role: %s", this.loggedInUser.getRole().toString().toLowerCase()));
    }

    public void setUser(User loggedInUser){
        this.loggedInUser = loggedInUser;
    }


    @Override
    public void displayView() {
        try {
            displayDateTime();
            displayUserInfo();
        }catch (Exception e){
            showError("an error occurred while loading the data");
        }

    }

    @Override
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
    }
}
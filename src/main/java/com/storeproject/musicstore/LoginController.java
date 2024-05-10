package com.storeproject.musicstore;

import com.storeproject.musicstore.data.Database;
import com.storeproject.musicstore.exceptions.LoadViewFailedException;
import com.storeproject.musicstore.interfaces.IController;
import com.storeproject.musicstore.model.User;
import com.storeproject.musicstore.exceptions.LoginFailedException;
import com.storeproject.musicstore.exceptions.AccountLockedException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LoginController implements IController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginErrorMessage;

    private Stage stage;

    private Database database;

    @Override
    public void displayView() {
        disableLoginButton();
    }

    @Override
    public void setDatabase(Database database){
        this.database = database;
    }

    @Override
    public void showError(String errorMessage) {
        loginErrorMessage.setText(errorMessage);
    }

    private void disableLoginButton() {
        loginButton.setDisable(true);
    }

    private void enableLoginButton() {
        loginButton.setDisable(false);
    }

    public void onPasswordTextChange() {
           if(isPasswordValid(passwordField.getText()))
              enableLoginButton();
           else disableLoginButton();
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            showError("Password should be 8 or more characters!");
            return false;
        }

        if (!password.matches(".*[a-zA-Z].*")) {
            showError("Password should contain a letter!");
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            showError("Password should contain a number!");
            return false;
        }

        if (!password.matches(".*[@#$%^&+=!].*")) {
            showError("Password should contain a special character!");
            return false;
        }
        showError("");
        return true;
    }

    private int attemptsCounter = 0;
    @FXML
    private void login() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        try {
            User loggedInUser = validateUser(username, password);
            try{
                openMainWindow(loggedInUser);
            } catch (LoadViewFailedException e){
                showError(e.getMessage());
            }
        } catch (LoginFailedException e) {
                showError(e.getMessage());
                usernameTextField.clear();
                passwordField.clear();
            try {
                attemptsCounter++;
                if(attemptsCounter >= 4) {
                    attemptsCounter = 0;
                    throw new AccountLockedException("Your account has been locked");
                }
            } catch (AccountLockedException ex) {
                showError(e.getMessage());
                blockAccount();
            }
        }
    }

    private void blockAccount(){
        showAccountLockDialog();
    }

    private void showAccountLockDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Account locked");
        alert.setHeaderText("Account Locked");
        alert.setContentText("Your account has been locked");

        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        }
        catch(NullPointerException e){
            System.out.println("Style resource not found");
        }

        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) loginButton.getScene().getWindow();
            stage.close();
        }
    }

    private User validateUser(String username, String password) throws LoginFailedException {
        List<User> users = database.getUsers();
        for(User user : users ){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        disableLoginButton();
        throw new LoginFailedException("password or username is incorrect");
    }

    private void openMainWindow(User loggedInUser) throws LoadViewFailedException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Music Store");

            MainController mainController = fxmlLoader.getController();
            mainController.setDatabase(database);
            mainController.setLoggedInUser(loggedInUser);
            mainController.displayView();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new LoadViewFailedException("failed to load the main view");
        }
    }
}
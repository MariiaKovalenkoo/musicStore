package com.storeproject.musicstore;

import com.storeproject.musicstore.data.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class LoginApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    private Database database;

    @Override
    public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            LoginController loginController = fxmlLoader.getController();

            // loading data
            database = new Database();
            File serializedFile = new File("database.ser");
            if (serializedFile.exists()) {
                database.deserializeData("database.ser");
            } else {
                database.createDefaultData();
            }
            loginController.setDatabase(database);
            loginController.displayView();

            try {
                Image icon = new Image((getClass().getResource("/com/storeproject/musicstore/images/guitar.png")).toExternalForm());
                stage.getIcons().add(icon);
            }
            catch(NullPointerException e){
                System.out.println("Icon not found");
            }

            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event -> {
                event.consume();
                logout(stage);
            });
    }

    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure?");

        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        }
        catch(NullPointerException e){
            System.out.println("Style resource not found");
        }

        if (alert.showAndWait().get() == ButtonType.OK) {
            database.serializeData();
            System.out.println("You successfully logged out");
            stage.close();
        }
    }
}
package com.example.kursovaya_aps;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Interface extends Application {
    public static void InterfaceLaunch(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("APS");

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Interface.fxml")));
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }
}

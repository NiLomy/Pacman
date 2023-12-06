package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.utils.AppConfig;

import java.io.IOException;

public class RoomsScreenController {
    @FXML
    private VBox panel;
    @FXML
    private Button room1;
    @FXML
    private Button room2;
    private static Scene scene;
    private static PacmanClient client;

    @FXML
    private void initialize() {
        room1.setOnAction(event -> {
            AppConfig.setHost(AppConfig.CURRENT_HOST);
            AppConfig.setPort(AppConfig.CURRENT_PORT_1);
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/waiting_room.fxml"));
            try {
                client = new PacmanClient(AppConfig.CURRENT_HOST, AppConfig.CURRENT_PORT_1);
                PacmanApplication.setClient(client);
                client.connect();
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                RoomsScreenController.scene = scene;
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        room2.setOnAction(event -> {
            AppConfig.setHost(AppConfig.CURRENT_HOST);
            AppConfig.setPort(AppConfig.CURRENT_PORT_2);
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/waiting_room.fxml"));
            try {
                client = new PacmanClient(AppConfig.CURRENT_HOST, AppConfig.CURRENT_PORT_2);
                PacmanApplication.setClient(client);
                client.connect();
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                RoomsScreenController.scene = scene;
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

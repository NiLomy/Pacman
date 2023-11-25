package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;

import java.io.IOException;

public class StartScreenController {
    @FXML
    private Button settings;
    @FXML
    private Button startPlay;
    private static Scene scene;

    @FXML
    private void initialize() {
        startPlay.setOnAction(event -> {
//            Stage stage = (Stage) startPlay.getScene().getWindow();
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/game_screen.fxml"));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                StartScreenController.scene = scene;
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static Scene getScene() {
        return scene;
    }
}

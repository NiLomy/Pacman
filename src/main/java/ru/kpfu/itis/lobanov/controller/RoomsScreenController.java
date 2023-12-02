package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
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
    @FXML
    private void initialize() {
        room1.setOnAction(event -> {
            AppConfig.setHost(AppConfig.CURRENT_HOST);
            AppConfig.setPort(AppConfig.CURRENT_PORT_1);
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/waiting_room.fxml"));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                RoomsScreenController.scene = scene;
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//        GameSettings.hostsCount.addListener((observable, oldValue, newValue) -> {
//            int newHostCount = newValue.intValue();
//            int oldHostCount = oldValue.intValue();
//
//            if (newHostCount > oldHostCount) {
//                Button button = new Button("Start game");
//                button.setId(AppConfig.CURRENT_HOST + " " + AppConfig.CURRENT_PORT_1);
//                button.setOnAction(event -> {
//
//                });
//                button.minWidth(100);
//                panel.getChildren().addAll(button);
//            } else {
//                Button b = (Button) panel.lookup("#" + AppConfig.CURRENT_HOST + " " + AppConfig.CURRENT_PORT_1);
//                panel.getChildren().remove(b);
//            }
//        });

//        GameSettings.hostsCount.set(GameSettings.hostsCount.intValue() + 1);
    }
}

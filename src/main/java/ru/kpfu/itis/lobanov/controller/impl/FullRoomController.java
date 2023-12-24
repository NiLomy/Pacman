package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;

import java.net.URL;
import java.util.ResourceBundle;

public class FullRoomController implements Controller {
    @FXML
    private AnchorPane screen;
    @FXML
    private Button backBtn;
    @FXML
    private Label fullRoomText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();
        backBtn.setOnAction(event -> visualizer.show(GameResources.ROOMS_SCREEN));

        if (AppConfig.lightMode) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
    }

    private void setLightTheme() {
        screen.setStyle("-fx-background-color: white");
        backBtn.getStyleClass().add("button-light");
        backBtn.setTextFill(Color.BLACK);
        fullRoomText.setTextFill(Color.BLACK);
    }

    private void setDarkTheme() {
        screen.setStyle("-fx-background-color: black");
        backBtn.getStyleClass().add("button-dark");
        backBtn.setTextFill(Color.BLUE);
        fullRoomText.setTextFill(Color.BLUE);
    }
}

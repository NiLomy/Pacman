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

public class NetErrorScreenController implements Controller {
    @FXML
    private AnchorPane screen;
    @FXML
    private Button goBackBtn;
    @FXML
    private Label lostConnectionText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();

        if (AppConfig.lightMode) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
        goBackBtn.setOnAction(event -> visualizer.show(GameResources.START_SCREEN));
    }

    private void setLightTheme() {
        screen.setStyle("-fx-background-color: white");
        goBackBtn.getStyleClass().add("button-light");
        goBackBtn.setTextFill(Color.BLACK);
        lostConnectionText.setTextFill(Color.BLACK);
    }

    private void setDarkTheme() {
        screen.setStyle("-fx-background-color: black");
        goBackBtn.getStyleClass().add("button-dark");
        goBackBtn.setTextFill(Color.BLUE);
        lostConnectionText.setTextFill(Color.BLUE);
    }
}

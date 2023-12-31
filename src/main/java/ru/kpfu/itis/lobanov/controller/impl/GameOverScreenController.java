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

public class GameOverScreenController implements Controller {
    private static final String SPACE_DIVIDER = "%s %s";
    @FXML
    private AnchorPane screen;
    @FXML
    private Label gameOverLabel;
    @FXML
    private Label statistics;
    @FXML
    private Label playerScoresLabel;
    @FXML
    private Label timePassedLabel;
    @FXML
    private Button goBackBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();

        if (AppConfig.lightMode) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
        gameOverLabel.setText(AppConfig.gameOverMessage);
        playerScoresLabel.setText(String.format(SPACE_DIVIDER, resources.getString(GameResources.GAME_SCORES), AppConfig.playerScores));
        timePassedLabel.setText(String.format(SPACE_DIVIDER, resources.getString(GameResources.GAME_PLAYED_TIME), AppConfig.timePassed));
        goBackBtn.setOnAction(event -> visualizer.show(GameResources.START_SCREEN));
    }

    private void setLightTheme() {
        screen.setStyle("-fx-background-color: white");
        goBackBtn.getStyleClass().add("button-light");
        goBackBtn.setTextFill(Color.BLACK);
        gameOverLabel.setTextFill(Color.BLACK);
        statistics.setTextFill(Color.BLACK);
        playerScoresLabel.setTextFill(Color.BLACK);
        timePassedLabel.setTextFill(Color.BLACK);
    }

    private void setDarkTheme() {
        screen.setStyle("-fx-background-color: black");
        goBackBtn.getStyleClass().add("button-dark");
        goBackBtn.setTextFill(Color.BLUE);
        gameOverLabel.setTextFill(Color.BLUE);
        statistics.setTextFill(Color.BLUE);
        playerScoresLabel.setTextFill(Color.BLUE);
        timePassedLabel.setTextFill(Color.BLUE);
    }
}

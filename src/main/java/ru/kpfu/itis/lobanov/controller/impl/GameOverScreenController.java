package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverScreenController implements Controller {
    private static final String SPACE_DIVIDER = "%s %s";
    @FXML
    private Label gameOverLabel;
    @FXML
    private Label playerScoresLabel;
    @FXML
    private Label timePassedLabel;
    @FXML
    private Button goBackBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();
        gameOverLabel.setText(AppConfig.gameOverMessage);
        playerScoresLabel.setText(String.format(SPACE_DIVIDER, resources.getString(GameResources.GAME_SCORES), AppConfig.playerScores));
        timePassedLabel.setText(String.format(SPACE_DIVIDER, resources.getString(GameResources.GAME_PLAYED_TIME), AppConfig.timePassed));
        goBackBtn.setOnAction(event -> visualizer.show(GameResources.START_SCREEN));
    }
}

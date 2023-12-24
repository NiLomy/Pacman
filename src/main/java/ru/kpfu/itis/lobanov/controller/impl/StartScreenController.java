package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.animation.GifAnimator;
import ru.kpfu.itis.lobanov.utils.animation.ImageAnimator;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartScreenController implements Controller {
    @FXML
    private AnchorPane screen;
    @FXML
    private Label gameName;
    @FXML
    private VBox imageBox;
    @FXML
    private Button settings;
    @FXML
    private Button startPlay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();

        if (AppConfig.lightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }

        ImageAnimator animator = new GifAnimator(Objects.requireNonNull(getClass().getResource(GameResources.PACMAN_GIF)).toExternalForm(), GameSettings.PACMAN_GIF_DURATION_MILLIS);
        animator.getView().setFitWidth(GameSettings.PACMAN_GIF_WIDTH);
        animator.getView().setFitHeight(GameSettings.PACMAN_GIF_HEIGHT);
        animator.setCycleCount(GameSettings.PACMAN_GIF_CYCLE_TIME_INDEFINITE);
        animator.play();
        imageBox.getChildren().addAll(animator.getView());

        startPlay.setOnAction(event -> visualizer.show(GameResources.ROOMS_SCREEN));
        settings.setOnAction(event -> visualizer.show(GameResources.SETTINGS_SCREEN));
    }

    private void setLightMode() {
        screen.setStyle("-fx-background-color: white");
        gameName.setTextFill(Color.BLACK);
        settings.getStyleClass().add("button-light");
        settings.setTextFill(Color.BLACK);
        startPlay.getStyleClass().add("button-light");
        startPlay.setTextFill(Color.BLACK);
    }

    private void setDarkMode() {
        screen.setStyle("-fx-background-color: black");
        gameName.setTextFill(Color.BLUE);
        settings.getStyleClass().add("button-dark");
        settings.setTextFill(Color.BLUE);
        startPlay.getStyleClass().add("button-dark");
        startPlay.setTextFill(Color.BLUE);
    }
}

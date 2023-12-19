package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.animation.GifAnimator;
import ru.kpfu.itis.lobanov.utils.animation.ImageAnimator;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartScreenController implements Controller {
    @FXML
    private VBox imageBox;
    @FXML
    private Button settings;
    @FXML
    private Button startPlay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();

        ImageAnimator animator = new GifAnimator(Objects.requireNonNull(getClass().getResource(GameResources.PACMAN_GIF)).toExternalForm(), GameSettings.PACMAN_GIF_DURATION_MILLIS);
        animator.getView().setFitWidth(GameSettings.PACMAN_GIF_WIDTH);
        animator.getView().setFitHeight(GameSettings.PACMAN_GIF_HEIGHT);
        animator.setCycleCount(GameSettings.PACMAN_GIF_CYCLE_TIME_INDEFINITE);
        animator.play();
        imageBox.getChildren().addAll(animator.getView());

        startPlay.setOnAction(event -> visualizer.show(GameResources.ROOMS_SCREEN));
        settings.setOnAction(event -> visualizer.show(GameResources.SETTINGS_SCREEN));
    }
}

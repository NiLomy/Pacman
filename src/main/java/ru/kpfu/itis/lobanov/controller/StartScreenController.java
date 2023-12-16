package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.utils.animation.GifAnimator;
import ru.kpfu.itis.lobanov.utils.animation.ImageAnimator;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
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
        ImageAnimator animator = new GifAnimator(Objects.requireNonNull(getClass().getResource("/images/pacman.gif")).toExternalForm(), 4000);
        animator.setCycleCount(-1);
        animator.play();
        animator.getView().setFitWidth(450);
        animator.getView().setFitHeight(250);
        imageBox.getChildren().addAll(animator.getView());
        startPlay.setOnAction(event -> {
            showScreen("/view/rooms_screen.fxml");
        });
        settings.setOnAction(event -> {
            showScreen("/view/settings_screen.fxml");
        });
    }

    private void showScreen(String fxmlName) {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(fxmlName));
        loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
            stage.setMaximized(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

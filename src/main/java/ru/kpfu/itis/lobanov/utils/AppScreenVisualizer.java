package ru.kpfu.itis.lobanov.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AppScreenVisualizer {
    public void show(String fxmlName) {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(fxmlName));
        loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/buttons.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/screens.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/sliders.css")).toExternalForm());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/text-fields.css")).toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsScreenController implements Controller {
    public static final String SMALL_SIZE = "small";
    public static final String MEDIUM_SIZE = "medium";
    public static final String LARGE_SIZE = "large";
    public static final String ENGLISH_LANGUAGE = "english";
    public static final String RUSSIAN_LANGUAGE = "russian";
    @FXML
    private ComboBox<String> sizeCombo;
    @FXML
    private ComboBox<String> languageCombo;
    @FXML
    private Button back;
    @FXML
    private Button apply;
    private double selectedCellSize;
    private Locale selectedLocal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (AppConfig.size == null) {
            sizeCombo.getSelectionModel().selectFirst();
        } else {
            sizeCombo.getSelectionModel().select(AppConfig.size);
        }
        if (AppConfig.language == null) {
            languageCombo.getSelectionModel().selectFirst();
        } else {
            languageCombo.getSelectionModel().select(AppConfig.language);
        }

        sizeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.toLowerCase().trim()) {
                    case SMALL_SIZE:
                        selectedCellSize = 20;
                        break;
                    case MEDIUM_SIZE:
                        selectedCellSize = (double) (20 + 25) / 2;
                        break;
                    case LARGE_SIZE:
                        selectedCellSize = 25;
                        break;
                }
                AppConfig.size = newValue;
            }
        });
        languageCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.toLowerCase().trim()) {
                    case ENGLISH_LANGUAGE:
                        selectedLocal = new Locale(GameResources.ENGLISH_LOCALIZATION);
                        break;
                    case RUSSIAN_LANGUAGE:
                        selectedLocal = new Locale(GameResources.RUSSIAN_LOCALIZATION);
                        break;
                }
                AppConfig.language = newValue;
            }
        });
        back.setOnAction(event -> {
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(GameResources.START_SCREEN));
            loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE, GameSettings.LOCALE));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        apply.setOnAction(event -> {
            if (selectedCellSize != 0) {
                GameSettings.CELL_SIZE = selectedCellSize;
            }
            if (selectedLocal != null) {
                GameSettings.LOCALE = selectedLocal;
            }
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(GameResources.SETTINGS_SCREEN));
            loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE, GameSettings.LOCALE));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

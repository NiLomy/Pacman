package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

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
    private AppScreenVisualizer visualizer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visualizer = new AppScreenVisualizer();

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
                        selectedCellSize = GameSettings.SMALL_CELL_SIZE;
                        break;
                    case MEDIUM_SIZE:
                        selectedCellSize = GameSettings.MEDIUM_CELL_SIZE;
                        break;
                    case LARGE_SIZE:
                        selectedCellSize = GameSettings.LARGE_CELL_SIZE;
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
        back.setOnAction(event -> visualizer.show(GameResources.START_SCREEN));
        apply.setOnAction(event -> {
            if (selectedCellSize != 0) {
                GameSettings.CELL_SIZE = selectedCellSize;
            }
            if (selectedLocal != null) {
                GameSettings.LOCALE = selectedLocal;
            }
            visualizer.show(GameResources.SETTINGS_SCREEN);
        });
    }
}

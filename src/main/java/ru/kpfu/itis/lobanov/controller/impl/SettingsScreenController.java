package ru.kpfu.itis.lobanov.controller.impl;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    private AnchorPane screen;
    @FXML
    private ComboBox<String> sizeCombo;
    @FXML
    private ComboBox<String> languageCombo;
    @FXML
    private Button back;
    @FXML
    private Button changeTheme;
    @FXML
    private Label changeThemeText;
    @FXML
    private Label changeSizeText;
    @FXML
    private Label changeLanguageText;
    @FXML
    private Button apply;
    private double selectedCellSize;
    private Locale selectedLocal;
    private AppScreenVisualizer visualizer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        visualizer = new AppScreenVisualizer();

        if (AppConfig.lightMode) {
            setLightMode(resources);
        } else {
            setDarkMode(resources);
        }
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
        changeTheme.setOnAction(event -> {
            AppConfig.lightMode = !AppConfig.lightMode;
            if (AppConfig.lightMode) {
                setLightMode(resources);
            } else {
                setDarkMode(resources);
            }
        });
        apply.setOnAction(event -> {
            if (selectedCellSize != 0) {
                AppConfig.CELL_SIZE = selectedCellSize;
            }
            if (selectedLocal != null) {
                AppConfig.LOCALE = selectedLocal;
            }
            visualizer.show(GameResources.SETTINGS_SCREEN);
        });
    }

    private void setLightMode(ResourceBundle resources) {
        screen.setStyle("-fx-background-color: white");
        changeThemeText.setTextFill(Color.BLACK);
        changeSizeText.setTextFill(Color.BLACK);
        changeLanguageText.setTextFill(Color.BLACK);
        changeThemeText.setText(resources.getString("game.mode.light"));
        ImageView imageView = new ImageView(new Image("/images/themes/light-mode.png"));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        changeTheme.setGraphic(imageView);
        back.getStyleClass().add("button-light");
        back.getStyleClass().remove("button-dark");
        back.setTextFill(Color.BLACK);
        changeTheme.getStyleClass().add("button-light");
        changeTheme.getStyleClass().remove("button-dark");
        changeTheme.setTextFill(Color.BLACK);
        apply.getStyleClass().add("button-light");
        apply.getStyleClass().remove("button-dark");
        apply.setTextFill(Color.BLACK);
        sizeCombo.getStyleClass().add("button-light");
        sizeCombo.getStyleClass().remove("button-dark");
        languageCombo.getStyleClass().add("button-light");
        languageCombo.getStyleClass().remove("button-dark");
    }

    private void setDarkMode(ResourceBundle resources) {
        screen.setStyle("-fx-background-color: black");
        changeThemeText.setTextFill(Color.BLUE);
        changeSizeText.setTextFill(Color.BLUE);
        changeLanguageText.setTextFill(Color.BLUE);
        changeThemeText.setText(resources.getString("game.mode.dark"));

        ImageView imageView = new ImageView(new Image("/images/themes/dark-mode.png"));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0.8);
        colorAdjust.setBrightness(0.8);

        imageView.setEffect(colorAdjust);
        changeTheme.setGraphic(imageView);
        back.getStyleClass().add("button-dark");
        back.setTextFill(Color.BLUE);
        changeTheme.getStyleClass().add("button-dark");
        changeTheme.setTextFill(Color.BLUE);
        apply.getStyleClass().add("button-dark");
        apply.setTextFill(Color.BLUE);
        sizeCombo.getStyleClass().add("button-dark");
        languageCombo.getStyleClass().add("button-dark");
    }
}

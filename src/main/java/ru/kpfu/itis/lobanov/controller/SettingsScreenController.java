package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsScreenController implements Controller {
    @FXML
    private ComboBox<String> sizeCombo;
    @FXML
    private ComboBox<String> languageCombo;
    @FXML
    private Button back;
    @FXML
    private Button apply;
    private ResourceBundle resources;
    private double selectedCellSize;
    private Locale selectedLocal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        sizeCombo.getSelectionModel().selectFirst();
        sizeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.toLowerCase().trim()) {
                    case "small":
                        selectedCellSize = 20;
                        break;
                    case "medium":
                        selectedCellSize = 30;
                        break;
                    case "large":
                        selectedCellSize = 40;
                        break;
                }
            }
        });
        languageCombo.getSelectionModel().selectFirst();
        languageCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switch (newValue.toLowerCase().trim()) {
                    case "english":
                        selectedLocal = new Locale("en");
                        break;
                    case "russian":
                        selectedLocal = new Locale("ru");
                        break;
                }
            }
        });
        back.setOnAction(event -> {
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/start_screen.fxml"));
            loader.setResources(ResourceBundle.getBundle("game_strings", GameSettings.LOCALE));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
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
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/settings_screen.fxml"));
            loader.setResources(ResourceBundle.getBundle("game_strings", GameSettings.LOCALE));
            try {
                AnchorPane pane = loader.load();
                Scene scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

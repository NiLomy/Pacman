package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FullRoomController implements Controller {
    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backBtn.setOnAction(event -> {
            Stage stage = PacmanApplication.getStage();
            FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/view/rooms_screen.fxml"));
            loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
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

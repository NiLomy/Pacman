package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;

import java.net.URL;
import java.util.ResourceBundle;

public class FullRoomController implements Controller {
    @FXML
    private Button backBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppScreenVisualizer visualizer = new AppScreenVisualizer();
        backBtn.setOnAction(event -> visualizer.show(GameResources.ROOMS_SCREEN));
    }
}

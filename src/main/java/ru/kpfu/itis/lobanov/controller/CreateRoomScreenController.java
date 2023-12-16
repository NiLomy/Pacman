package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.server.ApplicationServer;
import ru.kpfu.itis.lobanov.server.PacmanServer;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoomScreenController implements Controller {
    @FXML
    private Button backBtn;
    @FXML
    private Button changeBtn;
    @FXML
    private VBox panel;
    @FXML
    private TextField setPort;
    @FXML
    private Button submit;
    private PacmanServer server;
    private VBox map;
    private VBox lastMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = ApplicationServer.startServer(5678);
        backBtn.setOnAction(event -> goBack());
        changeBtn.setOnAction(event -> changeMap());
        submit.setOnAction(event -> launchServer());
        map = new VBox(10);
        map.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(map);
        changeMap();
    }

    private void goBack() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/view/start_screen.fxml"));
        loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ApplicationServer.closeServer(5678);
    }

    private void changeMap() {
        map.getChildren().remove(lastMap);
        map.getChildren().removeAll();
        server.setMaze(new Maze());
        String gameMap = server.generateWalls();
        showMap(gameMap);
    }

    private void showMap(String gameMap) {
        if (gameMap != null) {
            int index = 0;
            double mapLength = Math.sqrt(gameMap.length());
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            for (int x = 0; x < mapLength; x++) {
                HBox line = new HBox();
                line.setAlignment(Pos.CENTER);
                for (int y = 0; y < mapLength; y++) {
                    Rectangle rectangle = new Rectangle(x * 10, y * 10, 10, 10);
                    if (gameMap.charAt(index) == '0') {
                        rectangle.setFill(Color.LIGHTGREY);
                    }
                    line.getChildren().addAll(rectangle);
                    index++;
                }
                vBox.getChildren().addAll(line);
            }
            lastMap = vBox;
            map.getChildren().addAll(vBox);
        }
    }

    private void launchServer() {

    }
}

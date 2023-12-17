package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.model.entity.environment.Cell;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.server.ApplicationServer;
import ru.kpfu.itis.lobanov.server.PacmanServer;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
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
    @FXML
    private Slider setPlayerCount;
    @FXML
    private Label emptyPort;
    public static final String ANY_NON_DIGIT = "\\D*";
    private PacmanServer server;
    private VBox map;
    private VBox lastMap;
    private Maze maze;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maze = new Maze();
        setPlayerCount.setBlockIncrement(1);
        setPlayerCount.setMajorTickUnit(1);
        setPlayerCount.valueProperty().addListener((observable, oldValue, newValue) ->
                setPlayerCount.setValue(Math.round(newValue.doubleValue())));
        setPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (setPort.getText().length() > 4) {
                setPort.setText(setPort.getText().substring(0, 4));
            }
        });
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
        ApplicationServer.closeServer(5678);
    }

    private void changeMap() {
        map.getChildren().remove(lastMap);
        map.getChildren().removeAll();
        maze = new Maze();
        StringBuilder sb = new StringBuilder();
        Cell[][] cells = maze.getData();
        for (Cell[] cell : cells) {
            for (int j = 0; j < cells.length; j++) {
                if (cell[j].isWall()) {
                    sb.append(1);
                } else sb.append(0);
            }
        }
        showMap(sb.toString());
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
                    Rectangle rectangle = new Rectangle(x * 18, y * 18, 18, 18);
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
        String port = setPort.getText().trim();
        if (port.length() != 4 || port.matches(ANY_NON_DIGIT)) {
            emptyPort.setVisible(true);
        } else {
            emptyPort.setVisible(false);
            server = ApplicationServer.createServer(Integer.parseInt(port), (int) setPlayerCount.getValue());
            server.setMaze(maze);
            ApplicationServer.startServer(server);
            showWaitingRoomScreen(Integer.parseInt(port));
        }
    }

    private void showWaitingRoomScreen(int port) {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource(GameResources.WAITING_ROOM_SCREEN));
        loader.setResources(ResourceBundle.getBundle(GameResources.LOCALIZED_TEXTS_RESOURCE_BUNDLE, GameSettings.LOCALE));
        try {
            AppConfig.host = AppConfig.CURRENT_HOST;
            AppConfig.port = port;
            PacmanClient client = new PacmanClient(AppConfig.CURRENT_HOST, port);
            PacmanApplication.setClient(client);
            client.connect();
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

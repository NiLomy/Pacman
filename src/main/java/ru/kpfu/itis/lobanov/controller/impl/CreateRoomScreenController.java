package ru.kpfu.itis.lobanov.controller.impl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.model.entity.environment.Cell;
import ru.kpfu.itis.lobanov.model.entity.environment.Maze;
import ru.kpfu.itis.lobanov.server.ApplicationServer;
import ru.kpfu.itis.lobanov.server.PacmanServer;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.constants.Placement;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class CreateRoomScreenController implements Controller {
    public static final String ANY_NON_DIGIT = "\\D*";

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
    private VBox map;
    private VBox lastMap;
    private Maze maze;
    private AppScreenVisualizer visualizer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maze = new Maze();
        visualizer = new AppScreenVisualizer();

        setPlayerCount.setBlockIncrement(1);
        setPlayerCount.setMajorTickUnit(1);
        setPlayerCount.valueProperty().addListener((observable, oldValue, newValue) ->
                setPlayerCount.setValue(Math.round(newValue.doubleValue())));

        setPort.textProperty().addListener((observable, oldValue, newValue) -> {
            if (setPort.getText().length() > GameSettings.PORT_LENGTH) {
                setPort.setText(setPort.getText().substring(0, GameSettings.PORT_LENGTH));
            }
        });

        backBtn.setOnAction(event -> visualizer.show(GameResources.ROOMS_SCREEN));
        changeBtn.setOnAction(event -> changeMap());
        submit.setOnAction(event -> launchServer());

        map = new VBox(GameSettings.MAZE_BOX_SPACING);
        map.setAlignment(Pos.CENTER);
        panel.getChildren().addAll(map);
        changeMap();
    }

    private void changeMap() {
        map.getChildren().remove(lastMap);

        maze = new Maze();
        StringBuilder sb = new StringBuilder();
        Cell[][] cells = maze.getData();
        for (Cell[] cell : cells) {
            for (int j = 0; j < cells.length; j++) {
                if (cell[j].isWall()) {
                    sb.append(GameSettings.WALL_DECODER_CHAR);
                } else sb.append(GameSettings.SPACE_DECODER_CHAR);
            }
        }
        showMap(sb.toString());
    }

    private void showMap(String gameMap) {
        if (gameMap != null) {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);

            int index = 0;
            double mapLength = Math.sqrt(gameMap.length());
            for (int x = 0; x < mapLength; x++) {
                HBox line = new HBox();
                line.setAlignment(Pos.CENTER);

                for (int y = 0; y < mapLength; y++) {
                    Rectangle rectangle = new Rectangle(
                            x * GameSettings.MAZE_SIZE_FOR_CREATION_PREVIEW,
                            y * GameSettings.MAZE_SIZE_FOR_CREATION_PREVIEW,
                            GameSettings.MAZE_SIZE_FOR_CREATION_PREVIEW,
                            GameSettings.MAZE_SIZE_FOR_CREATION_PREVIEW);
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
        if (port.length() != GameSettings.PORT_LENGTH || port.matches(ANY_NON_DIGIT)) {
            emptyPort.setVisible(true);
        } else {
            emptyPort.setVisible(false);

//            new Thread(() -> {
//            }).start();
            PacmanServer server = ApplicationServer.createServer(Integer.parseInt(port), (int) setPlayerCount.getValue());
            server.setMaze(maze);
            ApplicationServer.startServer(server);
            Platform.runLater(() -> showWaitingRoomScreen(Integer.parseInt(port)));
        }
    }

    private void showWaitingRoomScreen(int port) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            AppConfig.host = host;
            AppConfig.port = port;

            PacmanClient client = new PacmanClient(host, port);
            PacmanApplication.setClient(client);
            client.connect();

            visualizer.show(GameResources.WAITING_ROOM_SCREEN);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

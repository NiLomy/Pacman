package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.controller.Controller;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;
import ru.kpfu.itis.lobanov.utils.AppScreenVisualizer;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameResources;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;
import ru.kpfu.itis.lobanov.utils.db.ServerRepository;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RoomsScreenController implements Controller {
    @FXML
    private AnchorPane screen;
    @FXML
    private VBox panel;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button backBtn;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button createRoom;
    private VBox serverBox;
    private ResourceBundle resources;
    private AppScreenVisualizer visualizer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        visualizer = new AppScreenVisualizer();

        refreshBtn.setOnAction(event -> visualizer.show(GameResources.ROOMS_SCREEN));
        backBtn.setOnAction(event -> visualizer.show(GameResources.START_SCREEN));
        createRoom.setOnAction(event -> visualizer.show(GameResources.CREATE_ROOM_SCREEN));

        List<ServerModel> servers = ServerRepository.getAll();
        if (servers.isEmpty()) {
            Label noRooms = new Label(resources.getString(GameResources.NO_ROOMS));
            noRooms.setStyle(GameResources.BIG_CENTER_TEXT);
            if (AppConfig.lightMode) {
                noRooms.setTextFill(Color.BLACK);
            } else {
                noRooms.setTextFill(Color.BLUE);
            }

            Button createRoomBtn = new Button(resources.getString(GameResources.ROOM_CREATE));
            createRoomBtn.setStyle(GameResources.MEDIUM_TEXT);
            if (AppConfig.lightMode) {
                createRoomBtn.getStyleClass().add("button-light");
                createRoomBtn.setTextFill(Color.BLACK);
            } else {
                createRoomBtn.getStyleClass().add("button-dark");
                createRoomBtn.setTextFill(Color.BLUE);
            }
            createRoomBtn.setOnAction(event -> visualizer.show(GameResources.CREATE_ROOM_SCREEN));

            scrollPane.setVisible(false);
            panel.getChildren().addAll(noRooms, createRoomBtn);
        } else {
            serverBox = new VBox(GameSettings.SERVER_BOX_SPACING);
            serverBox.setAlignment(Pos.CENTER);

            for (int i = 0; i < servers.size(); i++) {
                ServerModel server = servers.get(i);
                if (!server.isGameHeld()) createRoom(server.getHost(), server.getPort(), i + 1);
            }
            scrollPane.setContent(serverBox);
            scrollPane.setPannable(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setMaxHeight(GameSettings.SERVERS_SCROLL_PANE_MAX_HEIGHT);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            if (!AppConfig.lightMode) {
                scrollPane.setStyle("-fx-background-color: black");
            }
        }

        if (AppConfig.lightMode) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
    }

    private void createRoom(String host, int port, int position) {
        Button room = new Button(resources.getString(GameResources.ROOM_ENTER) + position);
        if (AppConfig.lightMode) {
            room.getStyleClass().add("button-light");
            room.setTextFill(Color.BLACK);
        } else {
            room.getStyleClass().add("button-dark");
            room.setTextFill(Color.BLUE);
        }
        room.setStyle(GameResources.MEDIUM_TEXT);
        room.setOnAction(event -> {
            if (ServerRepository.get(host, port).isGameHeld()) {
                visualizer.show(GameResources.FULL_ROOM_SCREEN);
            } else {
                try {
                    AppConfig.host = host;
                    AppConfig.port = port;

                    PacmanClient client = new PacmanClient(host, port);
                    PacmanApplication.setClient(client);
                    client.connect();

                    visualizer.show(GameResources.WAITING_ROOM_SCREEN);
                } catch (ClientException e) {
                    visualizer.show(GameResources.ROOMS_SCREEN);
                }
            }
        });
        showMap(host, port);
        serverBox.getChildren().addAll(room);
    }

    private void showMap(String host, int port) {
        String gameMap = ServerRepository.get(host, port).getGameMap();
        if (gameMap != null) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            int index = 0;
            double mapLength = Math.sqrt(gameMap.length());
            for (int x = 0; x < mapLength; x++) {
                VBox column = new VBox();
                column.setAlignment(Pos.CENTER);

                for (int y = 0; y < mapLength; y++) {
                    Rectangle rectangle = new Rectangle(
                            x * GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW,
                            y * GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW,
                            GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW,
                            GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW
                    );
                    if (AppConfig.lightMode) {
                        if (gameMap.charAt(index) == GameSettings.SPACE_DECODER_CHAR) {
                            rectangle.setFill(Color.LIGHTGREY);
                        }
                    } else {
                        if (gameMap.charAt(index) == GameSettings.WALL_DECODER_CHAR) {
                            rectangle.setFill(Color.BLUE);
                        }
                    }
                    column.getChildren().addAll(rectangle);
                    index++;
                }
                hbox.getChildren().addAll(column);
            }
            serverBox.getChildren().addAll(hbox);
        }
    }

    private void setLightTheme() {
        screen.setStyle("-fx-background-color: white");
        scrollPane.setStyle("-fx-background-color: white");
        backBtn.getStyleClass().add("button-light");
        backBtn.setTextFill(Color.BLACK);
        createRoom.getStyleClass().add("button-light");
        createRoom.setTextFill(Color.BLACK);
        refreshBtn.getStyleClass().add("button-light");
        refreshBtn.setTextFill(Color.BLACK);
        if (serverBox != null) serverBox.setStyle("-fx-background-color: white");
    }

    private void setDarkTheme() {
        screen.setStyle("-fx-background-color: black");
        scrollPane.setStyle("-fx-background-color: black");
        backBtn.getStyleClass().add("button-dark");
        backBtn.setTextFill(Color.BLUE);
        createRoom.getStyleClass().add("button-dark");
        createRoom.setTextFill(Color.BLUE);
        refreshBtn.getStyleClass().add("button-dark");
        refreshBtn.setTextFill(Color.BLUE);
        if (serverBox != null) serverBox.setStyle("-fx-background-color: black");
    }
}

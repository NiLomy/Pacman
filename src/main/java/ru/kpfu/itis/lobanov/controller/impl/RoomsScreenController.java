package ru.kpfu.itis.lobanov.controller.impl;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

            Button createRoomBtn = new Button(resources.getString(GameResources.ROOM_CREATE));
            createRoomBtn.setStyle(GameResources.MEDIUM_TEXT);
            createRoomBtn.setOnAction(event -> visualizer.show(GameResources.CREATE_ROOM_SCREEN));

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
        }
    }

    private void createRoom(String host, int port, int position) {
        Button room = new Button(resources.getString(GameResources.ROOM_ENTER) + position);
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
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);

            int index = 0;
            double mapLength = Math.sqrt(gameMap.length());
            for (int x = 0; x < mapLength; x++) {
                HBox line = new HBox();
                line.setAlignment(Pos.CENTER);

                for (int y = 0; y < mapLength; y++) {
                    Rectangle rectangle = new Rectangle(
                            x * GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW,
                            y * GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW,
                            GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW,
                            GameSettings.MAZE_SIZE_FOR_ROOMS_PREVIEW
                    );
                    if (gameMap.charAt(index) == GameSettings.SPACE_DECODER_CHAR) {
                        rectangle.setFill(Color.LIGHTGREY);
                    }
                    line.getChildren().addAll(rectangle);
                    index++;
                }
                vBox.getChildren().addAll(line);
            }
            serverBox.getChildren().addAll(vBox);
        }
    }
}

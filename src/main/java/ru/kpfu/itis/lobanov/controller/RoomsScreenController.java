package ru.kpfu.itis.lobanov.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.kpfu.itis.lobanov.PacmanApplication;
import ru.kpfu.itis.lobanov.client.PacmanClient;
import ru.kpfu.itis.lobanov.exceptions.ClientException;
import ru.kpfu.itis.lobanov.model.dao.ServerDao;
import ru.kpfu.itis.lobanov.model.dao.impl.ServerDaoImpl;
import ru.kpfu.itis.lobanov.model.entity.db.ServerModel;
import ru.kpfu.itis.lobanov.utils.constants.AppConfig;
import ru.kpfu.itis.lobanov.utils.constants.GameSettings;

import java.io.IOException;
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
    private static PacmanClient client;
    private ServerDao serverDao;
    private ResourceBundle resources;
    private VBox serverBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        refreshBtn.setOnAction(event -> refreshPage());
        backBtn.setOnAction(event -> goBack());
        createRoom.setOnAction(event -> showCreateRoomScreen());
        serverDao = new ServerDaoImpl();
        List<ServerModel> servers = serverDao.getAll();
        if (servers.isEmpty()) {
            Label noRooms = new Label(resources.getString("no_rooms"));
            noRooms.setStyle("-fx-font-weight: bold; -fx-font-size: 24");
            Button createRoomBtn = new Button(resources.getString("room.create"));
            createRoomBtn.setOnAction(event -> showCreateRoomScreen());
            panel.getChildren().addAll(noRooms, createRoomBtn);
        } else {
            serverBox = new VBox(10);
            serverBox.setAlignment(Pos.CENTER);
            for (int i = 0; i < servers.size(); i++) {
                ServerModel server = servers.get(i);
                if (!server.isGameHeld()) createRoom(server.getHost(), server.getPort(), i + 1);
            }
            scrollPane.setContent(serverBox);
            scrollPane.setPannable(true);
            scrollPane.setFitToWidth(true);
            scrollPane.setMaxHeight(400);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }
    }

    private void createRoom(String host, int port, int position) {
        Button room = new Button(resources.getString("room.enter") + position);
        room.setOnAction(event -> {
            if (serverDao.get(host, port).isGameHeld()) {
                showFullRoomScreen();
            } else {
                Stage stage = PacmanApplication.getStage();
                FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/view/waiting_room.fxml"));
                loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
                try {
                    AppConfig.host = host;
                    AppConfig.port = port;
                    client = new PacmanClient(host, port);
                    PacmanApplication.setClient(client);
                    client.connect();
                    AnchorPane pane = loader.load();
                    Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClientException e) {
                    refreshPage();
                }
            }
        });
        String gameMap = serverDao.get(host, port).getGameMap();
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
            serverBox.getChildren().addAll(vBox);
        }
        serverBox.getChildren().addAll(room);
    }

    private void refreshPage() {
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
    }

    private void showFullRoomScreen() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/view/full_room_screen.fxml"));
        loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showCreateRoomScreen() {
        Stage stage = PacmanApplication.getStage();
        FXMLLoader loader = new FXMLLoader(PacmanApplication.class.getResource("/view/create_room_screen.fxml"));
        loader.setResources(ResourceBundle.getBundle("property/game_strings", GameSettings.LOCALE));
        try {
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
